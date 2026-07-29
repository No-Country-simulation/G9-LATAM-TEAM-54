package com.energiai.backend.service;

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