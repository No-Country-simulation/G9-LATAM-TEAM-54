package com.energiai.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar un formato de correo válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}