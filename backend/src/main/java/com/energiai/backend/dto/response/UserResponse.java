package com.energiai.backend.dto.response;

import java.util.List;

public class UserResponse {
    private Long id;
    private String email;
    private String nombre;

    public UserResponse(Long id, String email, String nombre) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
}