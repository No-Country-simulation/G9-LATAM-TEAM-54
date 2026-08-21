package com.energiai.backend.service;

import com.energiai.backend.dto.request.ConfiguracionInicialRequest;
import com.energiai.backend.dto.request.UserRequest;
import com.energiai.backend.model.User;
import com.energiai.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registrarUsuario(UserRequest request) {
        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User actualizarConfiguracionInicial(String email, ConfiguracionInicialRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setAvgTemperatureC(request.getAvgTemperatureC());
        user.setHouseholdSize(request.getHouseholdSize());

        return userRepository.save(user);
    }
}