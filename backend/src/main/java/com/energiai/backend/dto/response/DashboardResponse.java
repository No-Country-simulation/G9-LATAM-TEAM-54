package com.energiai.backend.dto.response;

import java.util.List;

public class DashboardResponse {

    private double consumoTotal;
    private double costoTotal;
    private String categoria;
    private List<EstanciaDesgloseResponse> desgloseEstancias;
    private List<String> recomendaciones;

    public DashboardResponse() {
    }

    public DashboardResponse(double consumoTotal, double costoTotal, String categoria, List<EstanciaDesgloseResponse> desgloseEstancias, List<String> recomendaciones) {
        this.consumoTotal = consumoTotal;
        this.costoTotal = costoTotal;
        this.categoria = categoria;
        this.desgloseEstancias = desgloseEstancias;
        this.recomendaciones = recomendaciones;
    }

    public double getConsumoTotal() {
        return consumoTotal;
    }

    public void setConsumoTotal(double consumoTotal) {
        this.consumoTotal = consumoTotal;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<EstanciaDesgloseResponse> getDesgloseEstancias() {
        return desgloseEstancias;
    }

    public void setDesgloseEstancias(List<EstanciaDesgloseResponse> desgloseEstancias) {
        this.desgloseEstancias = desgloseEstancias;
    }

    public List<String> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(List<String> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }
}