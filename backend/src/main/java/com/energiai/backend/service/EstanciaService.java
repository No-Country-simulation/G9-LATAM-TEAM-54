package com.energiai.backend.service;

import com.energiai.backend.model.DispositivoUsuario;
import com.energiai.backend.model.User;
import com.energiai.backend.repository.DispositivoUsuarioRepository;
import com.energiai.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstanciaService {

    private final DispositivoUsuarioRepository dispositivoUsuarioRepository;
    private final CostService costService;
    private final UserRepository userRepository;

    public EstanciaService(DispositivoUsuarioRepository dispositivoUsuarioRepository,
                           CostService costService,
                           UserRepository userRepository) {
        this.dispositivoUsuarioRepository = dispositivoUsuarioRepository;
        this.costService = costService;
        this.userRepository = userRepository;
    }

    public Map<String, Object> calcularConsumoPorEstancia(String email, Long estanciaId) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + email));

        List<DispositivoUsuario> dispositivos = dispositivoUsuarioRepository.findByUserIdAndEstanciaId(usuario.getId(), estanciaId);

        double consumoTotalEstancia = dispositivos.stream()
                .mapToDouble(DispositivoUsuario::getConsumoMensualKwh)
                .sum();

        int cantidadEquipos = dispositivos.size();
        double costoEstimadoEstancia = costService.calcularCosto(consumoTotalEstancia);

        List<Map<String, Object>> dispositivosPlano = dispositivos.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("alias", d.getAlias());
            map.put("consumoMensualKwh", d.getConsumoMensualKwh());
            map.put("horasUsoDiarias", d.getHorasUsoDiarias());
            if (d.getEstancia() != null) {
                map.put("nombreEstancia", d.getEstancia().getNombre());
            }
            if (d.getEquipoCatalogo() != null) {
                map.put("nombreEquipo", d.getEquipoCatalogo().getNombre());
            }
            if (d.getEquipoVariante() != null) {
                map.put("nombreVariante", d.getEquipoVariante().getEtiqueta());
            }
            return map;
        }).collect(Collectors.toList());

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("estanciaId", estanciaId);
        resultado.put("cantidadEquipos", cantidadEquipos);
        resultado.put("consumoTotalKwh", consumoTotalEstancia);
        resultado.put("costoEstimadoEstancia", costoEstimadoEstancia);
        resultado.put("dispositivos", dispositivosPlano);

        return resultado;
    }
}
