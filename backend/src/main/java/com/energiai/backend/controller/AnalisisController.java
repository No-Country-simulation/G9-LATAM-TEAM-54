package com.energiai.backend.controller;

import com.energiai.backend.dto.request.DispositivoSeleccionRequest;
import com.energiai.backend.dto.response.AnalisisResponse;
import com.energiai.backend.dto.response.EstadisticasResponse;
import com.energiai.backend.dto.response.FinalAnalisisResponse;
import com.energiai.backend.service.AnalisisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnalisisController {

    private final AnalisisService analisisService;

    public AnalisisController(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @PostMapping("/analisis-energetico")
    public ResponseEntity<FinalAnalisisResponse> realizarAnalisisEnergetico(Principal principal) {
        String emailUsuario = principal.getName();
        FinalAnalisisResponse resultado = analisisService.ejecutarAnalisis(emailUsuario);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/analisis/calcular-dispositivo")
    public ResponseEntity<Map<String, Object>> calcularDispositivo(
            @Valid @RequestBody DispositivoSeleccionRequest request
    ) {
        Double consumoKwh = analisisService.calcularConsumoDispositivo(request);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Consumo calculado con éxito");
        response.put("consumoMensualKwh", consumoKwh);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/analisis/{id}")
    public ResponseEntity<Object> obtenerAnalisisPorId(@PathVariable("id") Long id, Principal principal) {
        String emailUsuario = principal.getName();

        return analisisService.obtenerPorId(id, emailUsuario)
                .map(entidad -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", entidad.getId());
                    response.put("categoria", entidad.getCategoria());
                    response.put("probabilidad", entidad.getProbabilidad());
                    response.put("consumoActual", entidad.getConsumoActual());
                    response.put("costoEstimado", entidad.getCostoEstimado());
                    response.put("recomendaciones", Arrays.asList(entidad.getRecomendaciones().split("\n")));
                    response.put("fechaCreacion", entidad.getFechaCreacion());
                    return ResponseEntity.ok((Object) response);
                })
                .orElse(ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/historial")
    public ResponseEntity<List<AnalisisResponse>> obtenerHistorialAutenticado(Principal principal) {
        String emailUsuario = principal.getName();
        List<AnalisisResponse> historial = analisisService.obtenerHistorialPorEmail(emailUsuario);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasResponse> obtenerEstadisticasAutenticado(Principal principal) {
        String emailUsuario = principal.getName();
        EstadisticasResponse stats = analisisService.obtenerEstadisticasPorEmail(emailUsuario);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/analisis/{id}")
    public ResponseEntity<Void> eliminarAnalisis(@PathVariable("id") Long id) {
        boolean eliminado = analisisService.eliminarAnalisis(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }
}