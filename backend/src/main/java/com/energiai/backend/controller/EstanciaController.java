package com.energiai.backend.controller;

import com.energiai.backend.model.Estancia;
import com.energiai.backend.repository.EstanciaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estancias")
public class EstanciaController {

    private final EstanciaRepository estanciaRepository;

    public EstanciaController(EstanciaRepository estanciaRepository) {
        this.estanciaRepository = estanciaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Estancia>> listarEstancias() {
        List<Estancia> estancias = estanciaRepository.findAll();
        return ResponseEntity.ok(estancias);
    }
}
