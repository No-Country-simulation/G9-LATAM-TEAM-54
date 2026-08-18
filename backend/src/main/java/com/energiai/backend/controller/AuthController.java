package com.energiai.backend.controller;

import com.energiai.backend.config.JwtTokenProvider;
import com.energiai.backend.dto.request.AuthRequest;
import com.energiai.backend.dto.response.AuthResponse;
import com.energiai.backend.model.User;
import com.energiai.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        // Usuario por correo
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        // BCrypt check
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        // JWT
        String token = tokenProvider.generarToken(user.getEmail());

        // Respuesta
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getNombre()));
    }
}