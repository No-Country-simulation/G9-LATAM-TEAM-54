package com.energiai.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfiguracionInicialRequest {

    @NotNull(message = "La temperatura promedio es obligatoria")
    private Double avgTemperatureC;

    @NotNull(message = "La cantidad de habitantes del hogar es obligatoria")
    private Integer householdSize;
}
