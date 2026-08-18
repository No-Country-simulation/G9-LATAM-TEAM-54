package com.energiai.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasResponse {
    private Long totalAnalisis;
    private Double consumoPromedioKwh;
    private Double costoPromedioMensual;
    private Double consumoTotalKwh;
    private Double costoTotalMensual;
}
