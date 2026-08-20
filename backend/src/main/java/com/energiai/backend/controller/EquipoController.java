package com.energiai.backend.controller;

import com.energiai.backend.dto.response.EquipoCatalogoResponse; // <--- Importa el DTO
import com.energiai.backend.service.EquipoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/equipos-catalogo")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public ResponseEntity<List<EquipoCatalogoResponse>> listarCatalogo() {
        List<EquipoCatalogoResponse> catalogo = equipoService.obtenerCatalogoCompleto();
        return ResponseEntity.ok(catalogo);
    }
}
