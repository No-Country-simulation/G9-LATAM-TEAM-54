package com.energiai.backend.controller;

import com.energiai.backend.dto.request.AnalisisRequest;
import com.energiai.backend.service.AnalisisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AnalisisController {

    private final AnalisisService analisisService;

    public AnalisisController(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @PostMapping("/analisis-energetico")
    public ResponseEntity<Object> realizarAnalisisEnergetico(@Valid @RequestBody AnalisisRequest requestDTO) {
        Object resultado = analisisService.ejecutarAnalisis(requestDTO);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/analisis/{id}")
    public ResponseEntity<Object> obtenerAnalisisPorId(@PathVariable Long id) {
        // Endpoint secundario para base de datos
        return ResponseEntity.status(501).body("Funcionalidad en desarrollo");
    }
}

