package com.energiai.backend.service;

import com.energiai.backend.model.CategoriaEnergetica;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationsService {

    public List<String> generarRecomendaciones(double consumoMensual, CategoriaEnergetica categoria, double prediccion) {
        List<String> recomendaciones = new ArrayList<>();

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

        double consumoDiarioEquivalente = consumoMensual / 30.0;

        if (consumoDiarioEquivalente > 18.0) {
            recomendaciones.add("Alerta: El consumo diario estimado supera los rangos eficientes permitidos.");
        }

        return recomendaciones;
    }
}