package com.energiai.backend.controller;

import com.energiai.backend.dto.response.EquipoCatalogoResponse;
import com.energiai.backend.dto.response.EquipoVarianteResponse;
import org.springframework.web.bind.annotation.PathVariable;
import com.energiai.backend.service.EquipoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final EquipoService equipoService;

    public CatalogoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public ResponseEntity<List<EquipoCatalogoResponse>> listarCatalogo() {
        List<EquipoCatalogoResponse> catalogo = equipoService.obtenerCatalogoCompleto();
        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/{id}/variantes")
    public ResponseEntity<List<EquipoVarianteResponse>> listarVariantesPorEquipo(@PathVariable Long id) {
        List<EquipoVarianteResponse> variantes = equipoService.obtenerVariantesPorEquipo(id);
        return ResponseEntity.ok(variantes);
    }
}
