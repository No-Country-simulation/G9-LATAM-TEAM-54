package com.energiai.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "equipo_catalogo")
public class EquipoCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String categoriaUso;

    @Column(nullable = false)
    private Boolean tieneVariantes;

    private Double potenciaBaseWatts;

    @OneToMany(mappedBy = "equipoCatalogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipoVariante> variantes;

    public EquipoCatalogo() {}

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

    public List<EquipoVariante> getVariantes() { return variantes; }
    public void setVariantes(List<EquipoVariante> variantes) { this.variantes = variantes; }
}
