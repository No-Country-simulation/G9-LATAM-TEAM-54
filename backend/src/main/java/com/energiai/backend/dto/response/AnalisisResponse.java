package com.energiai.backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisResponse {
    private String categoria;
    private Double probabilidad;
    private List<String> recomendaciones;
    private Double costoEstimadoMensual;
}