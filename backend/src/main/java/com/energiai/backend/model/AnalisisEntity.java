package com.energiai.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "analisis_energetico")
@Data
public class AnalisisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double consumoActual;
    private Double costoEstimado;
    private Double prediccion;
    private String categoria;
    private Double probabilidad;

    @Column(columnDefinition = "TEXT")
    private String recomendaciones;

    private LocalDateTime fechaCreacion = LocalDateTime.now();
}