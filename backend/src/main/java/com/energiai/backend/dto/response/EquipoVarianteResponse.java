package com.energiai.backend.dto.response;

public class EquipoVarianteResponse {
    private Long id;
    private String etiqueta;
    private Double potenciaWatts;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public Double getPotenciaWatts() { return potenciaWatts; }
    public void setPotenciaWatts(Double potenciaWatts) { this.potenciaWatts = potenciaWatts; }
}