package com.energiai.backend.dto.response;

import java.util.List;

public class EquipoCatalogoResponse {
    private Long id;
    private String nombre;
    private String categoriaUso;
    private Boolean tieneVariantes;
    private Double potenciaBaseWatts;
    private List<EquipoVarianteResponse> variantes;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCategoriaUso() { return categoriaUso; }
    public void setCategoriaUso(String categoriaUso) { this.categoriaUso = categoriaUso; }
    public Boolean getTieneVariantes() { return tieneVariantes; }
    public void setTieneVariantes(Boolean tieneVariantes) { this.tieneVariantes = tieneVariantes; }
    public Double getPotenciaBaseWatts() { return potenciaBaseWatts; }
    public void setPotenciaBaseWatts(Double potenciaBaseWatts) { this.potenciaBaseWatts = potenciaBaseWatts; }
    public List<EquipoVarianteResponse> getVariantes() { return variantes; }
    public void setVariantes(List<EquipoVarianteResponse> variantes) { this.variantes = variantes; }
}