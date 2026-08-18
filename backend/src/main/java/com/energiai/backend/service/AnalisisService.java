package com.energiai.backend.service;

import com.energiai.backend.dto.request.AnalisisRequest;
import com.energiai.backend.dto.request.DispositivoSeleccionRequest;
import com.energiai.backend.dto.response.AnalisisResponse;
import com.energiai.backend.dto.response.EstadisticasResponse;
import com.energiai.backend.model.AnalisisEntity;
import com.energiai.backend.model.CategoriaEnergetica;
import com.energiai.backend.model.DispositivoUsuario;
import com.energiai.backend.model.EquipoCatalogo;
import com.energiai.backend.model.EquipoVariante;
import com.energiai.backend.model.User;
import com.energiai.backend.repository.AnalisisRepository;
import com.energiai.backend.repository.DispositivoUsuarioRepository;
import com.energiai.backend.repository.EquipoCatalogoRepository;
import com.energiai.backend.repository.EquipoVarianteRepository;
import com.energiai.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnalisisService {

    private final PredictionService predictionService;
    private final CostService costService;
    private final RecommendationsService recommendationsService;
    private final AnalisisRepository analisisRepository;
    private final UserRepository userRepository;
    private final EquipoCatalogoRepository equipoCatalogoRepository;
    private final EquipoVarianteRepository equipoVarianteRepository;
    private final DispositivoUsuarioRepository dispositivoUsuarioRepository;

    public AnalisisService(PredictionService predictionService,
                           CostService costService,
                           RecommendationsService recommendationsService,
                           AnalisisRepository analisisRepository,
                           UserRepository userRepository,
                           EquipoCatalogoRepository equipoCatalogoRepository,
                           EquipoVarianteRepository equipoVarianteRepository,
                           DispositivoUsuarioRepository dispositivoUsuarioRepository) {
        this.predictionService = predictionService;
        this.costService = costService;
        this.recommendationsService = recommendationsService;
        this.analisisRepository = analisisRepository;
        this.userRepository = userRepository;
        this.equipoCatalogoRepository = equipoCatalogoRepository;
        this.equipoVarianteRepository = equipoVarianteRepository;
        this.dispositivoUsuarioRepository = dispositivoUsuarioRepository;
    }

    public Double calcularConsumoDispositivo(DispositivoSeleccionRequest request) {
        EquipoCatalogo equipo = equipoCatalogoRepository.findById(request.getEquipoCatalogoId())
                .orElseThrow(() -> new RuntimeException("Equipo de catálogo no encontrado"));

        Double potenciaWatts;

        if (Boolean.TRUE.equals(equipo.getTieneVariantes())) {
            if (request.getEquipoVarianteId() == null) {
                throw new IllegalArgumentException("Este equipo requiere seleccionar una variante específica (ej. BTU)");
            }

            EquipoVariante variante = equipoVarianteRepository.findById(request.getEquipoVarianteId())
                    .orElseThrow(() -> new RuntimeException("Variante de equipo no encontrada"));

            if (!variante.getEquipoCatalogo().getId().equals(equipo.getId())) {
                throw new IllegalArgumentException("La variante seleccionada no corresponde al equipo del catálogo");
            }

            potenciaWatts = variante.getPotenciaWatts();
        } else {
            if (equipo.getPotenciaBaseWatts() == null) {
                throw new IllegalStateException("El equipo no tiene configurada una potencia base");
            }
            potenciaWatts = equipo.getPotenciaBaseWatts();
        }

        return (potenciaWatts * request.getHorasUsoDiarias() * 30) / 1000.0;
    }

    public double calcularConsumoTotalInventario(Long userId) {
        return dispositivoUsuarioRepository.findByUserId(userId)
                .stream()
                .mapToDouble(DispositivoUsuario::getConsumoMensualKwh)
                .sum();
    }

    public Map<String, Object> ejecutarAnalisis(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + email));

        List<DispositivoUsuario> dispositivosUsuario = dispositivoUsuarioRepository.findByUserId(usuario.getId());

        int cantidadEquipos = dispositivosUsuario.size();
        double consumoMensual = dispositivosUsuario.stream()
                .mapToDouble(DispositivoUsuario::getConsumoMensualKwh)
                .sum();

        boolean hasAc = dispositivosUsuario.stream()
                .anyMatch(d -> d.getEquipoCatalogo() != null &&
                        d.getEquipoCatalogo().getNombre().toLowerCase().contains("aire"));

        int householdSize = usuario.getHouseholdSize() != null ? usuario.getHouseholdSize() : 3;
        double avgTemperatureC = usuario.getAvgTemperatureC() != null ? usuario.getAvgTemperatureC() : 28.0;

        double consumoDiarioEstimado = consumoMensual / 30.0;

        float peakHoursDiario = (float) dispositivosUsuario.stream()
                .mapToDouble(DispositivoUsuario::getHorasUsoDiarias)
                .average()
                .orElse(3.0);

        float[] inputData = new float[] {
                (float) consumoDiarioEstimado,
                (float) householdSize,
                (float) avgTemperatureC,
                hasAc ? 1.0f : 0.0f,
                peakHoursDiario
        };

        AnalisisRequest datosReales = new AnalisisRequest();
        datosReales.setHouseholdSize(householdSize);
        datosReales.setCantidadEquipos(cantidadEquipos);
        datosReales.setConsumo_kwh(consumoMensual);
        datosReales.setHasAc(hasAc);

        return ejecutarAnalisis(inputData, consumoMensual, datosReales, usuario);
    }

    @Transactional
    public Map<String, Object> ejecutarAnalisis(float[] inputData, double consumoMensual, AnalisisRequest request, User usuario) {
        PredictionService.PrediccionResultado resultadoPrediccion = predictionService.predecir(inputData);
        CategoriaEnergetica categoria = resultadoPrediccion.categoria();
        double probabilidad = resultadoPrediccion.probabilidad();

        double costoEstimado = costService.calcularCosto(consumoMensual);
        List<String> recomendaciones = recommendationsService.generarRecomendaciones(consumoMensual, categoria, 0.0);

        AnalisisEntity entidad = new AnalisisEntity();
        entidad.setConsumoActual(consumoMensual);
        entidad.setCostoEstimado(costoEstimado);
        entidad.setCategoria(categoria);
        entidad.setProbabilidad(probabilidad);
        entidad.setRecomendaciones(String.join("\n", recomendaciones));
        entidad.setUser(usuario);

        if (request != null) {
            entidad.setHouseholdSize(request.getHouseholdSize());
            entidad.setCantidadEquipos(request.getCantidadEquipos());
        }

        AnalisisEntity guardado = analisisRepository.save(entidad);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("id", guardado.getId());
        resultado.put("categoria", categoria);
        resultado.put("probabilidad", probabilidad);
        resultado.put("consumoActual", consumoMensual);
        resultado.put("costoEstimado", costoEstimado);
        resultado.put("recomendaciones", recomendaciones);

        if (request != null) {
            resultado.put("householdSize", request.getHouseholdSize());
            resultado.put("cantidadEquipos", request.getCantidadEquipos());
        }

        return resultado;
    }

    public Optional<AnalisisEntity> obtenerPorId(Long id, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + email));

        return analisisRepository.findById(id)
                .filter(entidad -> entidad.getUser() != null && entidad.getUser().getId().equals(usuario.getId()));
    }

    public List<AnalisisResponse> obtenerHistorialPorEmail(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + email));

        return analisisRepository.findByUserIdOrderByFechaCreacionDesc(usuario.getId())
                .stream()
                .map(entidad -> {
                    AnalisisResponse dto = new AnalisisResponse();
                    dto.setId(entidad.getId());
                    dto.setConsumoActual(entidad.getConsumoActual());
                    dto.setCostoEstimadoMensual(entidad.getCostoEstimado());
                    dto.setCategoria(entidad.getCategoria());
                    dto.setProbabilidad(entidad.getProbabilidad());
                    dto.setRecomendaciones(Arrays.asList(entidad.getRecomendaciones().split("\n")));
                    dto.setFechaCreacion(entidad.getFechaCreacion());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public EstadisticasResponse obtenerEstadisticasPorEmail(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + email));

        List<AnalisisEntity> historial = analisisRepository.findByUserIdOrderByFechaCreacionDesc(usuario.getId());

        if (historial.isEmpty()) {
            return new EstadisticasResponse(0L, 0.0, 0.0, 0.0, 0.0);
        }

        long total = historial.size();
        double sumaConsumo = historial.stream().mapToDouble(AnalisisEntity::getConsumoActual).sum();
        double sumaCosto = historial.stream().mapToDouble(AnalisisEntity::getCostoEstimado).sum();

        return new EstadisticasResponse(
                total,
                sumaConsumo / total,
                sumaCosto / total,
                sumaConsumo,
                sumaCosto
        );
    }

    @Transactional
    public boolean eliminarAnalisis(Long id) {
        if (analisisRepository.existsById(id)) {
            analisisRepository.deleteById(id);
            return true;
        }
        return false;
    }
}