package com.energiai.backend.dto.response;

public class DispositivoResponse {

    private Long id;
    private String alias;
    private String nombreEquipo;
    private String nombreVariante;
    private Double horasUsoDiarias;
    private Double consumoMensualKwh;

    public DispositivoResponse() {}

    public DispositivoResponse(Long id, String alias, String nombreEquipo, String nombreVariante, Double horasUsoDiarias, Double consumoMensualKwh) {
        this.id = id;
        this.alias = alias;
        this.nombreEquipo = nombreEquipo;
        this.nombreVariante = nombreVariante;
        this.horasUsoDiarias = horasUsoDiarias;
        this.consumoMensualKwh = consumoMensualKwh;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }

    public String getNombreVariante() { return nombreVariante; }
    public void setNombreVariante(String nombreVariante) { this.nombreVariante = nombreVariante; }

    public Double getHorasUsoDiarias() { return horasUsoDiarias; }
    public void setHorasUsoDiarias(Double horasUsoDiarias) { this.horasUsoDiarias = horasUsoDiarias; }

    public Double getConsumoMensualKwh() { return consumoMensualKwh; }
    public void setConsumoMensualKwh(Double consumoMensualKwh) { this.consumoMensualKwh = consumoMensualKwh; }
}