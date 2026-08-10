package com.energiai.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotNull(message = "El indicador de uso en horario pico es obligatorio")
    private Boolean uso_horario_pico;

    @NotNull(message = "La cantidad de equipos es obligatoria")
    @Min(value = 1, message = "La cantidad de equipos debe ser al menos 1")
    private Integer cantidad_equipos;

    @NotBlank(message = "El tipo de inmueble no puede estar vacío")
    private String tipo_inmueble;

    @NotNull(message = "Las horas de alto consumo son obligatorias")
    @Min(value = 0, message = "Las horas de alto consumo no pueden ser negativas")
    private Integer horas_alto_consumo;

}