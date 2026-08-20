package com.energiai.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "opciones_temperatura")
public class OpcionTemperatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String etiqueta;

    @Column(nullable = false)
    private Integer valorNumerico;

    private Integer orden;

    private Boolean activo;

    public OpcionTemperatura() {}

    public OpcionTemperatura(String etiqueta, Integer valorNumerico, Integer orden, Boolean activo) {
        this.etiqueta = etiqueta;
        this.valorNumerico = valorNumerico;
        this.orden = orden;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Integer getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(Integer valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}