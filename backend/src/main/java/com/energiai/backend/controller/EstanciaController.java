package com.energiai.backend.controller;

import com.energiai.backend.model.Estancia;
import com.energiai.backend.repository.EstanciaRepository;
import com.energiai.backend.service.EstanciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estancias")
public class EstanciaController {

    private final EstanciaRepository estanciaRepository;
    private final EstanciaService estanciaService;

    public EstanciaController(EstanciaRepository estanciaRepository, EstanciaService estanciaService) {
        this.estanciaRepository = estanciaRepository;
        this.estanciaService = estanciaService;
    }

    @GetMapping
    public ResponseEntity<List<Estancia>> listarEstancias() {
        List<Estancia> estancias = estanciaRepository.findAll();
        return ResponseEntity.ok(estancias);
    }

    @GetMapping("/{id}/analisis")
    public ResponseEntity<Map<String, Object>> analizarEstancia(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Map<String, Object> analisis = estanciaService.calcularConsumoPorEstancia(email, id);
        return ResponseEntity.ok(analisis);
    }
}
