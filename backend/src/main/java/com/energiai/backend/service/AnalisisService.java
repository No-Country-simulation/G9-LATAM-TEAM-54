package com.energiai.backend.service;

import com.energiai.backend.dto.request.AnalisisRequest;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalisisService {

    private final PredictionService predictionService;
    private final CostService costService;
    private final RecommendationsService recommendationsService;

    public AnalisisService(PredictionService predictionService,
                           CostService costService,
                           RecommendationsService recommendationsService) {
        this.predictionService = predictionService;
        this.costService = costService;
        this.recommendationsService = recommendationsService;
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

        // 3. Generar recomendaciones
        List<String> recomendaciones = recommendationsService.generarRecomendaciones(consumoActual, prediccionFloat);

        // 4. Empaquetar el resultado
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("consumoActual", consumoActual);
        resultado.put("prediccion", prediccionFloat);
        resultado.put("costoEstimado", costoEstimado);
        resultado.put("recomendaciones", recomendaciones);

        return resultado;
    }
}