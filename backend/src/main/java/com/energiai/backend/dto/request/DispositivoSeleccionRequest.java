package com.energiai.backend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class DispositivoSeleccionRequest {

    @NotNull(message = "El ID del equipo del catálogo es obligatorio")
    private Long equipoCatalogoId;

    private Long equipoVarianteId;

    @NotNull(message = "Las horas de uso diario son obligatorias")
    @DecimalMin(value = "0.1", message = "El uso diario debe ser al menos de 0.1 horas")
    @DecimalMax(value = "24.0", message = "El uso diario no puede superar las 24 horas")
    private Double horasUsoDiarias;

    private String alias; // Opcional

    // Getters y Setters
    public Long getEquipoCatalogoId() { return equipoCatalogoId; }
    public void setEquipoCatalogoId(Long equipoCatalogoId) { this.equipoCatalogoId = equipoCatalogoId; }

    public Long getEquipoVarianteId() { return equipoVarianteId; }
    public void setEquipoVarianteId(Long equipoVarianteId) { this.equipoVarianteId = equipoVarianteId; }

    public Double getHorasUsoDiarias() { return horasUsoDiarias; }
    public void setHorasUsoDiarias(Double horasUsoDiarias) { this.horasUsoDiarias = horasUsoDiarias; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
}