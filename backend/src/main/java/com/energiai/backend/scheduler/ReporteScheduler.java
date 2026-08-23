package com.energiai.backend.scheduler;

import com.energiai.backend.model.DispositivoUsuario;
import com.energiai.backend.model.User;
import com.energiai.backend.repository.DispositivoUsuarioRepository;
import com.energiai.backend.repository.UserRepository;
import com.energiai.backend.service.AnalisisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Tarea programada para la generación periódica y automática de reportes energéticos.
 * Emula el corte mensual de consumo de los usuarios activos en la plataforma.
 */
@Component
public class ReporteScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ReporteScheduler.class);

    private final UserRepository userRepository;
    private final DispositivoUsuarioRepository dispositivoUsuarioRepository;
    private final AnalisisService analisisService;

    public ReporteScheduler(UserRepository userRepository,
                            DispositivoUsuarioRepository dispositivoUsuarioRepository,
                            AnalisisService analisisService) {
        this.userRepository = userRepository;
        this.dispositivoUsuarioRepository = dispositivoUsuarioRepository;
        this.analisisService = analisisService;
    }

    /**
     * Tarea periódica programada.
     * Por defecto se ejecuta a la medianoche (00:00:00) del primer día de cada mes ("0 0 0 1 * *").
     * El patrón cron puede sobreescribirse mediante la propiedad `app.scheduler.reportes.cron`.
     */
    @Scheduled(cron = "${app.scheduler.reportes.cron:0 0 0 1 * *}")
    public void ejecutarCorteMensualReportes() {
        long inicioMs = System.currentTimeMillis();
        logger.info("================================================================================");
        logger.info("[ReporteScheduler] Iniciando tarea programada de corte mensual de consumo...");
        logger.info("================================================================================");

        List<User> usuarios = userRepository.findAll();
        int totalUsuarios = usuarios.size();
        int reportesGenerados = 0;
        int usuariosSinDispositivos = 0;
        int errores = 0;

        for (User usuario : usuarios) {
            try {
                // Consultar dispositivos activos respetando el borrado lógico (activo = true)
                List<DispositivoUsuario> dispositivosActivos = dispositivoUsuarioRepository.findByUserId(usuario.getId());

                if (dispositivosActivos.isEmpty()) {
                    logger.debug("[ReporteScheduler] Usuario ID: {} ({}) sin dispositivos activos registrados. Omitiendo generación de reporte.",
                            usuario.getId(), usuario.getEmail());
                    usuariosSinDispositivos++;
                    continue;
                }

                // Generar y persistir el reporte mensual para el usuario
                analisisService.ejecutarAnalisis(usuario.getEmail());
                reportesGenerados++;

                logger.info("[ReporteScheduler] Reporte mensual generado exitosamente para usuario ID: {} ({}) con {} dispositivos activos.",
                        usuario.getId(), usuario.getEmail(), dispositivosActivos.size());

            } catch (Exception e) {
                errores++;
                logger.error("[ReporteScheduler] Error al procesar el reporte mensual para usuario ID: {} ({}): {}",
                        usuario.getId(), usuario.getEmail(), e.getMessage(), e);
            }
        }

        long duracionMs = System.currentTimeMillis() - inicioMs;
        logger.info("================================================================================");
        logger.info("[ReporteScheduler] Tarea programada finalizada en {} ms.", duracionMs);
        logger.info("[ReporteScheduler] Resumen: Total usuarios: {}, Reportes generados: {}, Sin equipos: {}, Errores: {}",
                totalUsuarios, reportesGenerados, usuariosSinDispositivos, errores);
        logger.info("================================================================================");
    }
}
