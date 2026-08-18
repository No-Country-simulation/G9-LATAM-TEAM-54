package com.energiai.backend.dto.response;

import com.energiai.backend.model.CategoriaEnergetica;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisResponse {
    private Long id;
    private Double consumoActual;
    private Double costoEstimadoMensual;
    private CategoriaEnergetica categoria;
    private Double probabilidad;
    private List<String> recomendaciones;
    private LocalDateTime fechaCreacion;
}