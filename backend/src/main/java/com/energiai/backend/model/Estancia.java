package com.energiai.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estancias")
@Data
public class Estancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;
}