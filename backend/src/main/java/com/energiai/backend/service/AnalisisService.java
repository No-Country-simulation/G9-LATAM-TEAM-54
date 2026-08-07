package com.energiai.backend.service;

import com.energiai.backend.dto.request.AnalisisRequest;
import com.energiai.backend.model.AnalisisEntity;
import com.energiai.backend.repository.AnalisisRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalisisService {

    private final PredictionService predictionService;
    private final CostService costService;
    private final RecommendationsService recommendationsService;
    private final AnalisisRepository analisisRepository;

    public AnalisisService(PredictionService predictionService,
                           CostService costService,
                           RecommendationsService recommendationsService,
                           AnalisisRepository analisisRepository) {
        this.predictionService = predictionService;
        this.costService = costService;
        this.recommendationsService = recommendationsService;
        this.analisisRepository = analisisRepository;
    }

    // Método adaptador que procesa el DTO y llama a la lógica principal
    public Map<String, Object> ejecutarAnalisis(AnalisisRequest request) {
        double consumoActual = request.getConsumo_kwh();

        // Mapea los campos del DTO
        float[] inputData = new float[] {
                request.getConsumo_kwh() != null ? request.getConsumo_kwh().floatValue() : 0.0f,
                Boolean.TRUE.equals(request.getUso_horario_pico()) ? 1.0f : 0.0f,
                request.getCantidad_equipos() != null ? request.getCantidad_equipos().floatValue() : 0.0f,
                request.getHoras_alto_consumo() != null ? request.getHoras_alto_consumo().floatValue() : 0.0f
        };

        return ejecutarAnalisis(inputData, consumoActual);
    }

    public Map<String, Object> ejecutarAnalisis(float[] inputData, double consumoActual) {
        // 1. Invocar el modelo ONNX
        float prediccionFloat = predictionService.predecir(inputData);

        // 2. Calcular el costo
        double costoEstimado = costService.calcularCosto(consumoActual);

        // 3. Lógica temporal de clasificación
        String categoria;
        double probabilidad;

        if (consumoActual <= 200) {
            categoria = "Eficiente";
            probabilidad = 0.90;
        } else if (consumoActual <= 400) {
            categoria = "Moderado";
            probabilidad = 0.82;
        } else {
            categoria = "Ineficiente";
            probabilidad = 0.85;
        }

        // 4. Generar recomendaciones
        List<String> recomendaciones = recommendationsService.generarRecomendaciones(consumoActual, prediccionFloat);

        // 5. GUARDAR EN DB
        AnalisisEntity entidad = new AnalisisEntity();
        entidad.setConsumoActual(consumoActual);
        entidad.setPrediccion((double) prediccionFloat);
        entidad.setCostoEstimado(costoEstimado);
        entidad.setCategoria(categoria);
        entidad.setProbabilidad(probabilidad);
        entidad.setRecomendaciones(String.join("\n", recomendaciones));

        AnalisisEntity guardado = analisisRepository.save(entidad);

        // 6. Empaquetar el resultado
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("id", guardado.getId());
        resultado.put("categoria", categoria);
        resultado.put("probabilidad", probabilidad);
        resultado.put("consumoActual", consumoActual);
        resultado.put("costoEstimado", costoEstimado);
        resultado.put("recomendaciones", recomendaciones);

        return resultado;
    }
}