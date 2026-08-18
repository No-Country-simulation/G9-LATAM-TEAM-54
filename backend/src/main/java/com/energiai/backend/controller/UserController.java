package com.energiai.backend.controller;

import com.energiai.backend.dto.request.UserRequest;
import com.energiai.backend.dto.response.UserResponse;
import com.energiai.backend.model.User;
import com.energiai.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> registrarUsuario(@Valid @RequestBody UserRequest request) {
        User nuevoUsuario = userService.registrarUsuario(request);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listarUsuarios() {
        List<UserResponse> usuarios = userService.listarUsuarios().stream()
                .map(user -> new UserResponse(user.getId(), user.getEmail(), user.getNombre()))
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> obtenerPerfilActual(Principal principal) {
        String email = principal.getName();
        User usuario = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserResponse response = new UserResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre()
        );

        return ResponseEntity.ok(response);
    }
}