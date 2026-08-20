package com.energiai.backend.controller;

import com.energiai.backend.dto.response.DashboardResponse;
import com.energiai.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/actual")
    public ResponseEntity<DashboardResponse> obtenerEstadoActual(Principal principal) {
        String email = principal.getName();

        DashboardResponse resumen = dashboardService.calcularEstadoActualPorUsuario(email);

        return ResponseEntity.ok(resumen);
    }
}
