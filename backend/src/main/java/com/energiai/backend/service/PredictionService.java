package com.energiai.backend.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Map;

@Service
public class PredictionService {

    private OrtEnvironment env;
    private OrtSession session;

    @PostConstruct
    public void init() {
        try {
            env = OrtEnvironment.getEnvironment();

            // Carga el modelo ONNX
            try (InputStream modelStream = getClass().getClassLoader().getResourceAsStream("models/modelo.onnx")) {
                if (modelStream == null) {
                    throw new RuntimeException("No se encontró el archivo del modelo ONNX en resources/models/modelo.onnx");
                }

                byte[] modelBytes = modelStream.readAllBytes();
                session = env.createSession(modelBytes);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar el modelo ONNX: " + e.getMessage(), e);
        }
    }

    public float predecir(float[] inputData) {
        try {
            // Tensor de entrada
            long[] shape = new long[]{1, inputData.length};
            FloatBuffer floatBuffer = FloatBuffer.wrap(inputData);

            try (OnnxTensor tensor = OnnxTensor.createTensor(env, floatBuffer, shape)) {
                String inputName = session.getInputNames().iterator().next();
                Map<String, OnnxTensor> inputs = Collections.singletonMap(inputName, tensor);

                try (OrtSession.Result result = session.run(inputs)) {
                    // Extraccion de resultados
                    float[][] output = (float[][]) result.get(0).getValue();
                    return output[0][0];
                }
            }
        } catch (OrtException e) {
            throw new RuntimeException("Error durante la ejecución de la inferencia ONNX: " + e.getMessage(), e);
        }
    }

    @PreDestroy
    public void close() {
        try {
            if (session != null) session.close();
            if (env != null) env.close();
        } catch (OrtException e) {
            // Manejar cierre si es necesario
        }
    }
}