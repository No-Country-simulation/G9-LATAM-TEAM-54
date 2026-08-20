package com.energiai.backend.dto.response;

import com.energiai.backend.model.CategoriaEnergetica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalAnalisisResponse {
    private Long id;
    private CategoriaEnergetica categoria;
    private Double probabilidad;
    private Double consumoActual;
    private Double costoEstimado;
    private List<String> recomendaciones;
    private List<EstanciaDesgloseResponse> desgloseEstancias;
    private Integer householdSize;
    private Integer cantidadEquipos;
}
