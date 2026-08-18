package com.energiai.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dispositivos_usuario")
public class DispositivoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_catalogo_id", nullable = false)
    private EquipoCatalogo equipoCatalogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_variante_id")
    private EquipoVariante equipoVariante;

    @Column(nullable = false)
    private Double horasUsoDiarias;

    @Column(nullable = false)
    private Double consumoMensualKwh;

    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estancia_id")
    private Estancia estancia;

    // Constructores
    public DispositivoUsuario() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public EquipoCatalogo getEquipoCatalogo() { return equipoCatalogo; }
    public void setEquipoCatalogo(EquipoCatalogo equipoCatalogo) { this.equipoCatalogo = equipoCatalogo; }

    public EquipoVariante getEquipoVariante() { return equipoVariante; }
    public void setEquipoVariante(EquipoVariante equipoVariante) { this.equipoVariante = equipoVariante; }

    public Double getHorasUsoDiarias() { return horasUsoDiarias; }
    public void setHorasUsoDiarias(Double horasUsoDiarias) { this.horasUsoDiarias = horasUsoDiarias; }

    public Double getConsumoMensualKwh() { return consumoMensualKwh; }
    public void setConsumoMensualKwh(Double consumoMensualKwh) { this.consumoMensualKwh = consumoMensualKwh; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public Estancia getEstancia() { return estancia; }
    public void setEstancia(Estancia estancia) { this.estancia = estancia; }
}