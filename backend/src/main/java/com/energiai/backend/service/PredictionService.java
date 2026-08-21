package com.energiai.backend.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.energiai.backend.model.CategoriaEnergetica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PredictionService {

    private static final Logger logger = LoggerFactory.getLogger(PredictionService.class);

    private OrtEnvironment env;
    private OrtSession session;

    private final OciObjectStorageService ociObjectStorageService;

    public PredictionService(OciObjectStorageService ociObjectStorageService) {
        this.ociObjectStorageService = ociObjectStorageService;
    }

    public record PrediccionResultado(CategoriaEnergetica categoria, double probabilidad) {}

    @PostConstruct
    public void init() {
        try {
            env = OrtEnvironment.getEnvironment();
            byte[] modelBytes = loadModelBytes();
            if (modelBytes == null) {
                logger.warn("No se pudo obtener el modelo ONNX (ni desde OCI ni desde el classpath). Usando modo simulación.");
                return;
            }
            session = env.createSession(modelBytes);
            logger.info("¡Modelo ONNX cargado exitosamente en el contenedor!");
        } catch (Exception e) {
            logger.error("No se pudo inicializar el modelo ONNX: {}", e.getMessage(), e);
        }
    }

    /**
     * Intenta obtener el modelo primero desde el bucket de OCI Object Storage.
     * Si OCI no está habilitado/disponible, cae de vuelta al archivo empaquetado en el classpath.
     */
    private byte[] loadModelBytes() throws Exception {
        if (ociObjectStorageService.isAvailable()) {
            try {
                logger.info("Descargando modelo ONNX desde OCI Object Storage...");
                return ociObjectStorageService.downloadModel();
            } catch (Exception e) {
                logger.error("Falló la descarga del modelo desde OCI Object Storage, se usará el respaldo local: {}",
                        e.getMessage(), e);
            }
        }

        try (InputStream modelStream = getClass().getClassLoader().getResourceAsStream("EnergiAI_model.onnx")) {
            if (modelStream == null) {
                return null;
            }
            return modelStream.readAllBytes();
        }
    }

    public PrediccionResultado predecir(float[] inputData) {
        if (inputData == null || inputData.length == 0) {
            logger.warn("Input data nulo o vacío. Retornando valores por defecto.");
            return formatResult(CategoriaEnergetica.MODERADO, 0.5);
        }

        float consumoActual = inputData[0];

        if (consumoActual < 0.0f) {
            logger.warn(">>> Anomalía detectada: Consumo negativo ({} kWh). Forzando EFICIENTE", consumoActual);
            return formatResult(CategoriaEnergetica.EFICIENTE, 1.0);
        }

        if (session == null) {
            logger.debug("Ejecutando predicción en modo simulación (modelo no disponible).");
            CategoriaEnergetica cat = CategoriaEnergetica.MODERADO;
            if (consumoActual < 10.0f) cat = CategoriaEnergetica.EFICIENTE;
            else if (consumoActual > 18.0f) cat = CategoriaEnergetica.INEFICIENTE;
            return formatResult(cat, 0.90);
        }

        try {
            long[] shape = new long[]{1, inputData.length};
            FloatBuffer floatBuffer = FloatBuffer.wrap(inputData);

            try (OnnxTensor tensor = OnnxTensor.createTensor(env, floatBuffer, shape)) {
                String inputName = session.getInputNames().iterator().next();
                Map<String, OnnxTensor> inputs = Collections.singletonMap(inputName, tensor);

                try (OrtSession.Result result = session.run(inputs)) {
                    String stringOutput = extractLabel(result);
                    double probabilidad = extractProbability(result, stringOutput);

                    return formatResult(parseCategoria(stringOutput), probabilidad);
                }
            }
        } catch (OrtException e) {
            logger.error("Error detallado de ONNX Runtime: [Mensaje: {}] [Causa: {}]", e.getMessage(), e.getCause(), e);
            throw new RuntimeException("Error en la inferencia del modelo: " + e.getMessage(), e);
        }
    }

    private String extractLabel(OrtSession.Result result) throws OrtException {
        var labelOpt = result.get("output_label");
        if (labelOpt.isPresent()) {
            Object rawOutput = labelOpt.get().getValue();
            if (rawOutput instanceof Object[] outerArray) {
                if (outerArray.length > 0) {
                    if (outerArray[0] instanceof Object[] innerArray && innerArray.length > 0) {
                        return innerArray[0].toString();
                    }
                    if (outerArray[0] != null) {
                        return outerArray[0].toString();
                    }
                }
            } else if (rawOutput != null) {
                return rawOutput.toString();
            }
        }
        return "MODERADO";
    }

    private double extractProbability(OrtSession.Result result, String targetLabel) throws OrtException {
        var probOpt = result.get("output_probability");
        if (probOpt.isPresent()) {
            Map<?, ?> targetMap = extractTargetMap(probOpt.get().getValue());
            if (targetMap != null) {
                for (Map.Entry<?, ?> entry : targetMap.entrySet()) {
                    if (entry.getKey() != null && entry.getKey().toString().equalsIgnoreCase(targetLabel)) {
                        if (entry.getValue() instanceof Number num) {
                            return num.doubleValue();
                        }
                    }
                }
            }
        }
        return 0.85;
    }

    private Map<?, ?> extractTargetMap(Object probValue) {
        if (probValue instanceof List<?> list && !list.isEmpty()) {
            Object firstElement = list.getFirst();
            try {
                var getValueMethod = firstElement.getClass().getMethod("getValue");
                Object innerMapObj = getValueMethod.invoke(firstElement);
                if (innerMapObj instanceof Map<?, ?> map) {
                    return map;
                }
            } catch (Exception ignored) {}

            if (firstElement instanceof Map<?, ?> map) {
                return map;
            }
        } else if (probValue instanceof Map<?, ?> map) {
            return map;
        }
        return null;
    }

    private PrediccionResultado formatResult(CategoriaEnergetica categoria, double probabilidad) {
        double roundedProb = Math.round(probabilidad * 100.0) / 100.0;
        return new PrediccionResultado(categoria, roundedProb);
    }

    private CategoriaEnergetica parseCategoria(String value) {
        try {
            return CategoriaEnergetica.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Valor de categoría no reconocido del modelo: '{}'. Usando MODERADO por defecto.", value);
            return CategoriaEnergetica.MODERADO;
        }
    }

    @PreDestroy
    public void close() {
        try {
            if (session != null) session.close();
            if (env != null) env.close();
            logger.info("Recursos de ONNX liberados correctamente.");
        } catch (OrtException e) {
            logger.error("Error al cerrar los recursos de ONNX: {}", e.getMessage());
        }
    }
}