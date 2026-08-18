package com.energiai.backend.repository;

import com.energiai.backend.model.EquipoVariante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoVarianteRepository extends JpaRepository<EquipoVariante, Long> {
}