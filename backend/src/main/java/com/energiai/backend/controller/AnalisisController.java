package com.energiai.backend.controller;

import com.energiai.backend.dto.request.AnalisisRequest;
import com.energiai.backend.repository.AnalisisRepository;
import com.energiai.backend.service.AnalisisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AnalisisController {

    private final AnalisisService analisisService;
    private final AnalisisRepository analisisRepository;

    public AnalisisController(AnalisisService analisisService, AnalisisRepository analisisRepository) {
        this.analisisService = analisisService;
        this.analisisRepository = analisisRepository;
    }

    @PostMapping("/analisis-energetico")
    public ResponseEntity<Object> realizarAnalisisEnergetico(@Valid @RequestBody AnalisisRequest requestDTO) {
        Object resultado = analisisService.ejecutarAnalisis(requestDTO);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/analisis/{id}")
    public ResponseEntity<Object> obtenerAnalisisPorId(@PathVariable("id") Long id) {
        return analisisRepository.findById(id)
                .map(entidad -> {
                    java.util.Map<String, Object> response = new java.util.HashMap<>();
                    response.put("id", entidad.getId());
                    response.put("categoria", entidad.getCategoria());
                    response.put("probabilidad", entidad.getProbabilidad());
                    response.put("consumoActual", entidad.getConsumoActual());
                    response.put("costoEstimado", entidad.getCostoEstimado());
                    response.put("prediccion", entidad.getPrediccion());
                    response.put("recomendaciones", java.util.Arrays.asList(entidad.getRecomendaciones().split("\n")));
                    response.put("fechaCreacion", entidad.getFechaCreacion());
                    return ResponseEntity.ok((Object) response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
