package com.energiai.backend.service;

import com.energiai.backend.dto.response.DashboardResponse;
import com.energiai.backend.dto.response.DispositivoResponse;
import com.energiai.backend.dto.response.EstanciaDesgloseResponse;
import com.energiai.backend.model.DispositivoUsuario;
import com.energiai.backend.model.User;
import com.energiai.backend.repository.DispositivoUsuarioRepository;
import com.energiai.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final DispositivoUsuarioRepository dispositivoUsuarioRepository;
    private final CostService costService;
    private final PredictionService predictionService;
    private final RecommendationsService recommendationsService;

    public DashboardService(
            UserRepository userRepository,
            DispositivoUsuarioRepository dispositivoUsuarioRepository,
            CostService costService,
            PredictionService predictionService,
            RecommendationsService recommendationsService
    ) {
        this.userRepository = userRepository;
        this.dispositivoUsuarioRepository = dispositivoUsuarioRepository;
        this.costService = costService;
        this.predictionService = predictionService;
        this.recommendationsService = recommendationsService;
    }

    @Transactional(readOnly = true)
    public DashboardResponse calcularEstadoActualPorUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

        List<DispositivoUsuario> dispositivos = dispositivoUsuarioRepository.findByUserId(usuario.getId());

        double consumoTotal = dispositivos.stream()
                .mapToDouble(DispositivoUsuario::getConsumoMensualKwh)
                .sum();

        double costoTotal = costService.calcularCosto(consumoTotal);

        double consumoDiarioEstimado = consumoTotal / 30.0;

        boolean hasAc = dispositivos.stream()
                .anyMatch(d -> d.getEquipoCatalogo() != null &&
                        d.getEquipoCatalogo().getNombre().toLowerCase().contains("aire"));

        float peakHoursDiario = (float) dispositivos.stream()
                .mapToDouble(DispositivoUsuario::getHorasUsoDiarias)
                .average()
                .orElse(3.0);

        int householdSize = usuario.getHouseholdSize() != null ? usuario.getHouseholdSize() : 3;
        double avgTemperatureC = usuario.getAvgTemperatureC() != null ? usuario.getAvgTemperatureC() : 28.0;

        float[] inputFeatures = new float[] {
                (float) consumoDiarioEstimado,
                (float) householdSize,
                (float) avgTemperatureC,
                hasAc ? 1.0f : 0.0f,
                peakHoursDiario
        };

        PredictionService.PrediccionResultado prediccion = predictionService.predecir(inputFeatures);
        String categoriaStr = prediccion.categoria().name();

        List<String> recomendaciones = recommendationsService.generarRecomendaciones(
                consumoTotal,
                prediccion.categoria(),
                prediccion.probabilidad()
        );

        Map<Long, List<DispositivoUsuario>> porEstancia = dispositivos.stream()
                .filter(d -> d.getEstancia() != null)
                .collect(Collectors.groupingBy(d -> d.getEstancia().getId()));

        List<EstanciaDesgloseResponse> desgloseEstancias = porEstancia.entrySet().stream().map(entry -> {
            Long estanciaId = entry.getKey();
            List<DispositivoUsuario> dispEstancia = entry.getValue();

            String nombreEstancia = dispEstancia.get(0).getEstancia().getNombre();

            double consumoEstancia = dispEstancia.stream()
                    .mapToDouble(DispositivoUsuario::getConsumoMensualKwh)
                    .sum();

            double costoEstancia = costService.calcularCosto(consumoEstancia);

            List<DispositivoResponse> dispositivosResponse = dispEstancia.stream().map(d -> {
                DispositivoResponse dr = new DispositivoResponse();
                dr.setId(d.getId());
                dr.setAlias(d.getAlias());
                dr.setConsumoMensualKwh(d.getConsumoMensualKwh());
                dr.setHorasUsoDiarias(d.getHorasUsoDiarias());

                if (d.getEstancia() != null) {
                    dr.setNombreEstancia(d.getEstancia().getNombre());
                }
                if (d.getEquipoCatalogo() != null) {
                    dr.setNombreEquipo(d.getEquipoCatalogo().getNombre());
                }
                if (d.getEquipoVariante() != null) {
                    dr.setNombreVariante(d.getEquipoVariante().getEtiqueta());
                }

                return dr;
            }).collect(Collectors.toList());

            return new EstanciaDesgloseResponse(estanciaId, nombreEstancia, consumoEstancia, costoEstancia, dispositivosResponse);
        }).collect(Collectors.toList());

        DashboardResponse response = new DashboardResponse();
        response.setConsumoTotal(consumoTotal);
        response.setCostoTotal(costoTotal);
        response.setCategoria(categoriaStr);
        response.setDesgloseEstancias(desgloseEstancias);
        response.setRecomendaciones(recomendaciones);

        return response;
    }
}