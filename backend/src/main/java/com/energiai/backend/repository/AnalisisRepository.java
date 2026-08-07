package com.energiai.backend.repository;

import com.energiai.backend.model.AnalisisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalisisRepository extends JpaRepository<AnalisisEntity, Long> {
}