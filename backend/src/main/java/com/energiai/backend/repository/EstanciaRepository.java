package com.energiai.backend.repository;

import com.energiai.backend.model.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, Long> {

    Optional<Estancia> findByNombre(String nombre);

}