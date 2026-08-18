package com.energiai.backend.service;

import com.energiai.backend.dto.request.DispositivoSeleccionRequest;
import com.energiai.backend.dto.response.DispositivoResponse;
import com.energiai.backend.model.DispositivoUsuario;
import com.energiai.backend.model.EquipoCatalogo;
import com.energiai.backend.model.EquipoVariante;
import com.energiai.backend.model.User;
import com.energiai.backend.repository.DispositivoUsuarioRepository;
import com.energiai.backend.repository.EquipoCatalogoRepository;
import com.energiai.backend.repository.EquipoVarianteRepository;
import com.energiai.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispositivoService {

    private final DispositivoUsuarioRepository repository;
    private final EquipoCatalogoRepository catalogoRepository;
    private final EquipoVarianteRepository varianteRepository;
    private final AnalisisService analisisService;
    private final UserRepository userRepository;

    public DispositivoService(DispositivoUsuarioRepository repository,
                              EquipoCatalogoRepository catalogoRepository,
                              EquipoVarianteRepository varianteRepository,
                              AnalisisService analisisService,
                              UserRepository userRepository) {
        this.repository = repository;
        this.catalogoRepository = catalogoRepository;
        this.varianteRepository = varianteRepository;
        this.analisisService = analisisService;
        this.userRepository = userRepository;
    }

    @Transactional
    public DispositivoResponse guardarDispositivo(String email, DispositivoSeleccionRequest request) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        EquipoCatalogo equipo = catalogoRepository.findById(request.getEquipoCatalogoId())
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        Double consumoKwh = analisisService.calcularConsumoDispositivo(request);

        DispositivoUsuario dispositivo = new DispositivoUsuario();
        dispositivo.setUser(usuario);
        dispositivo.setEquipoCatalogo(equipo);
        dispositivo.setHorasUsoDiarias(request.getHorasUsoDiarias());
        dispositivo.setConsumoMensualKwh(consumoKwh);
        dispositivo.setAlias(request.getAlias());

        // Si hay variante, asignarla
        if (request.getEquipoVarianteId() != null) {
            EquipoVariante variante = varianteRepository.findById(request.getEquipoVarianteId())
                    .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
            dispositivo.setEquipoVariante(variante);
        }

        DispositivoUsuario guardado = repository.save(dispositivo);
        return mapToResponse(guardado);
    }

    /**
     * Lista todos los dispositivos guardados
     */
    public List<DispositivoResponse> listarDispositivos(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return repository.findByUserId(usuario.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Elimina un dispositivo
     */
    @Transactional
    public boolean eliminarDispositivo(Long id, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return repository.findById(id)
                .filter(d -> d.getUser().getId().equals(usuario.getId()))
                .map(d -> {
                    repository.delete(d);
                    return true;
                })
                .orElse(false);
    }

    private DispositivoResponse mapToResponse(DispositivoUsuario entidad) {
        String nombreEquipo = entidad.getEquipoCatalogo() != null ? entidad.getEquipoCatalogo().getNombre() : null;

        // Usamos getEtiqueta() en lugar de getNombre() para la variante
        String nombreVariante = entidad.getEquipoVariante() != null ? entidad.getEquipoVariante().getEtiqueta() : null;

        return new DispositivoResponse(
                entidad.getId(),
                entidad.getAlias(),
                nombreEquipo,
                nombreVariante,
                entidad.getHorasUsoDiarias(),
                entidad.getConsumoMensualKwh()
        );
    }
}
