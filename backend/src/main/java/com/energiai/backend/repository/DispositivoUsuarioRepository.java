package com.energiai.backend.repository;

import com.energiai.backend.model.DispositivoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispositivoUsuarioRepository extends JpaRepository<DispositivoUsuario, Long> {

    List<DispositivoUsuario> findByUserId(Long userId);

    List<DispositivoUsuario> findByUserIdAndEstanciaId(Long userId, Long estanciaId);

}
