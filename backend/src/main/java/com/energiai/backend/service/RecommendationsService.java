package com.energiai.backend.service;

import com.energiai.backend.model.CategoriaEnergetica;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationsService {

    public List<String> generarRecomendaciones(double consumo, CategoriaEnergetica categoria, double prediccion) {
        List<String> recomendaciones = new ArrayList<>();

        // 1. Recomendación según la categoría
        switch (categoria) {
            case INEFICIENTE:
                recomendaciones.add("El consumo actual es elevado. Considere apagar equipos pesados o de alto consumo en horas pico.");
                break;
            case EFICIENTE:
                recomendaciones.add("El consumo se encuentra dentro del rango eficiente óptimo.");
                break;
            case MODERADO:
            default:
                recomendaciones.add("El consumo se encuentra en un rango moderado y equilibrado.");
                break;
        }

        // 2. Validación
        if (consumo > 18.0) {
            recomendaciones.add("Alerta: El consumo actual supera los rangos eficientes permitidos.");
        }

        return recomendaciones;
    }
}