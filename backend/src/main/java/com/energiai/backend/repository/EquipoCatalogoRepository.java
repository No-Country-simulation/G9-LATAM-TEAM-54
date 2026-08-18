package com.energiai.backend.repository;

import com.energiai.backend.model.EquipoCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoCatalogoRepository extends JpaRepository<EquipoCatalogo, Long> {
}
