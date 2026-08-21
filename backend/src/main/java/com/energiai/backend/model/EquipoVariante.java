package com.energiai.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "equipo_variante")
public class EquipoVariante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_catalogo_id", nullable = false)
    private EquipoCatalogo equipoCatalogo;

    @Column(nullable = false)
    private String etiqueta; // Ej: "9,000 BTU", "12,000 BTU"

    @Column(nullable = false)
    private Double potenciaWatts; // Ej: 900.0, 1200.0

    public EquipoVariante() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EquipoCatalogo getEquipoCatalogo() { return equipoCatalogo; }
    public void setEquipoCatalogo(EquipoCatalogo equipoCatalogo) { this.equipoCatalogo = equipoCatalogo; }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }

    public Double getPotenciaWatts() { return potenciaWatts; }
    public void setPotenciaWatts(Double potenciaWatts) { this.potenciaWatts = potenciaWatts; }
}