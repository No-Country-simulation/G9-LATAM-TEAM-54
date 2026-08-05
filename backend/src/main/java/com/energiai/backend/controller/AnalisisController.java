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
    public ResponseEntity<Object> obtenerAnalisisPorId(@PathVariable("id") Long id) {
        // Simulacion de resultados
        System.out.println("AVISO: Usando respuesta GET simulada para el ID: " + id);

        java.util.Map<String, Object> mockResponse = new java.util.HashMap<>();
        mockResponse.put("id", id);
        mockResponse.put("consumoActual", 150.0);
        mockResponse.put("costoEstimado", 112.5);
        mockResponse.put("prediccion", 42.0);
        mockResponse.put("recomendaciones", java.util.List.of("El consumo actual es elevado. Considere apagar equipos en horas pico."));

        return ResponseEntity.ok(mockResponse);
    }
}

