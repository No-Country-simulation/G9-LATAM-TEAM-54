package com.energiai.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisRequest {

    @NotNull(message = "El consumo en kWh es obligatorio")
    @Min(value = 0, message = "El consumo en kWh no puede ser negativo")
    private Double consumo_kwh;

    @NotNull(message = "El tamaño del hogar es obligatorio")
    @Min(value = 1, message = "El tamaño del hogar debe ser al menos 1")
    private Integer householdSize;

    @NotNull(message = "La temperatura promedio es obligatoria")
    private Double avgTemperatureC;

    @NotNull(message = "El indicador de aire acondicionado es obligatorio")
    private Boolean hasAc;

    @NotNull(message = "El uso en horas pico en kWh es obligatorio")
    @Min(value = 0, message = "El consumo en horas pico no puede ser negativo")
    private Double peakHoursUsageKwh;

    @NotNull(message = "La cantidad de equipos es obligatoria")
    @Min(value = 0, message = "La cantidad de equipos no puede ser negativa")
    private Integer cantidadEquipos;
}