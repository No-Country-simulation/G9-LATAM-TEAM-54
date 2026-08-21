package com.energiai.backend.controller;

import com.energiai.backend.model.OpcionTemperatura;
import com.energiai.backend.repository.OpcionTemperaturaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/configuracion")
public class OpcionTemperaturaController {

    private final OpcionTemperaturaRepository opcionTemperaturaRepository;

    public OpcionTemperaturaController(OpcionTemperaturaRepository opcionTemperaturaRepository) {
        this.opcionTemperaturaRepository = opcionTemperaturaRepository;
    }

    @GetMapping("/opciones-temperatura")
    public ResponseEntity<List<OpcionTemperatura>> obtenerOpcionesTemperatura() {
        List<OpcionTemperatura> opciones = opcionTemperaturaRepository.findByActivoTrueOrderByOrdenAsc();
        return ResponseEntity.ok(opciones);
    }
}
