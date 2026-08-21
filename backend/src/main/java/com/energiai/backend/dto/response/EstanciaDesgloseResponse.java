package com.energiai.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstanciaDesgloseResponse {
    private Long id;
    private String nombreEstancia;
    private Double consumoKwh;
    private Double costo;
    private List<DispositivoResponse> dispositivos;
}