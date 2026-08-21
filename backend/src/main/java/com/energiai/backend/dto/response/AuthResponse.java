package com.energiai.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private String email;
    private String nombre;

    public AuthResponse(String token, String email, String nombre) {
        this.token = token;
        this.email = email;
        this.nombre = nombre;
    }
}
