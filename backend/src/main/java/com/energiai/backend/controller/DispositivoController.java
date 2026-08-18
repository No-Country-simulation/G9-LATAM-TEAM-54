package com.energiai.backend.controller;

import com.energiai.backend.dto.request.DispositivoSeleccionRequest;
import com.energiai.backend.dto.response.DispositivoResponse;
import com.energiai.backend.service.DispositivoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {

    private final DispositivoService dispositivoService;

    public DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<DispositivoResponse> guardarDispositivo(
            @Valid @RequestBody DispositivoSeleccionRequest request,
            Principal principal
    ) {
        DispositivoResponse guardado = dispositivoService.guardarDispositivo(principal.getName(), request);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<DispositivoResponse>> listarMisDispositivos(Principal principal) {
        List<DispositivoResponse> dispositivos = dispositivoService.listarDispositivos(principal.getName());
        return ResponseEntity.ok(dispositivos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDispositivo(@PathVariable Long id, Principal principal) {
        boolean eliminado = dispositivoService.eliminarDispositivo(id, principal.getName());
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}