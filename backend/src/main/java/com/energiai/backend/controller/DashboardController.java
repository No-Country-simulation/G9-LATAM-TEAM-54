package com.energiai.backend.controller;

import com.energiai.backend.dto.response.DashboardResponse;
import com.energiai.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/actual")
    public ResponseEntity<DashboardResponse> obtenerEstadoActual(Principal principal) {
        String email = principal.getName();
        DashboardResponse resumen = dashboardService.calcularEstadoActualPorUsuario(email);
        return ResponseEntity.ok(resumen);
    }
}
