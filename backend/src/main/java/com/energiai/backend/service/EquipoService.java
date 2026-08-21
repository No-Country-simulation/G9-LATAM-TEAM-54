package com.energiai.backend.service;

import com.energiai.backend.dto.response.EquipoCatalogoResponse;
import com.energiai.backend.dto.response.EquipoVarianteResponse;
import com.energiai.backend.model.EquipoCatalogo;
import com.energiai.backend.repository.EquipoCatalogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    private final EquipoCatalogoRepository equipoCatalogoRepository;

    public EquipoService(EquipoCatalogoRepository equipoCatalogoRepository) {
        this.equipoCatalogoRepository = equipoCatalogoRepository;
    }

    public List<EquipoCatalogoResponse> obtenerCatalogoCompleto() {
        return equipoCatalogoRepository.findAll().stream().map(entidad -> {
            EquipoCatalogoResponse dto = new EquipoCatalogoResponse();
            dto.setId(entidad.getId());
            dto.setNombre(entidad.getNombre());
            dto.setCategoriaUso(entidad.getCategoriaUso());
            dto.setTieneVariantes(entidad.getTieneVariantes());
            dto.setPotenciaBaseWatts(entidad.getPotenciaBaseWatts());

            if (entidad.getVariantes() != null) {
                List<EquipoVarianteResponse> variantesDto = entidad.getVariantes().stream().map(v -> {
                    EquipoVarianteResponse vDto = new EquipoVarianteResponse();
                    vDto.setId(v.getId());
                    vDto.setEtiqueta(v.getEtiqueta());
                    vDto.setPotenciaWatts(v.getPotenciaWatts());
                    return vDto;
                }).collect(Collectors.toList());
                dto.setVariantes(variantesDto);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public List<EquipoVarianteResponse> obtenerVariantesPorEquipo(Long equipoId) {
        return equipoCatalogoRepository.findById(equipoId)
                .map(equipo -> {
                    if (equipo.getVariantes() == null) return List.<EquipoVarianteResponse>of();
                    return equipo.getVariantes().stream().map(v -> {
                        EquipoVarianteResponse vDto = new EquipoVarianteResponse();
                        vDto.setId(v.getId());
                        vDto.setEtiqueta(v.getEtiqueta());
                        vDto.setPotenciaWatts(v.getPotenciaWatts());
                        return vDto;
                    }).collect(Collectors.toList());
                })
                .orElse(List.of());
    }
}
