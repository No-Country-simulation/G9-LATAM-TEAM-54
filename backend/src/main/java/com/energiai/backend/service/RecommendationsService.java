package com.energiai.backend.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationsService {

    public List<String> generarRecomendaciones(double consumo, double prediccion) {
        List<String> recomendaciones = new ArrayList<>();

        if (consumo > 100.0) {
            recomendaciones.add("El consumo actual es elevado. Considere apagar equipos en horas pico.");
        } else {
            recomendaciones.add("El consumo se encuentra dentro de los rangos normales optimizados.");
        }

        if (prediccion > 150.0) {
            recomendaciones.add("Alerta: La predicción futura indica un incremento notable en la demanda energética.");
        }

        return recomendaciones;
    }
}