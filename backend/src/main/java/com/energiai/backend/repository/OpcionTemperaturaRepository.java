package com.energiai.backend.repository;

import com.energiai.backend.model.OpcionTemperatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionTemperaturaRepository extends JpaRepository<OpcionTemperatura, Long> {

    List<OpcionTemperatura> findByActivoTrueOrderByOrdenAsc();
}
