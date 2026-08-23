package com.energiai.backend.repository;

import com.energiai.backend.model.DispositivoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispositivoUsuarioRepository extends JpaRepository<DispositivoUsuario, Long> {

    /** Sólo retorna dispositivos activos (no eliminados lógicamente). */
    @Query("SELECT d FROM DispositivoUsuario d " +
           "LEFT JOIN FETCH d.equipoCatalogo " +
           "LEFT JOIN FETCH d.equipoVariante " +
           "LEFT JOIN FETCH d.estancia " +
           "WHERE d.user.id = :userId AND d.activo = true")
    List<DispositivoUsuario> findByUserId(@Param("userId") Long userId);

    /** Sólo retorna dispositivos activos en una estancia específica. */
    @Query("SELECT d FROM DispositivoUsuario d " +
           "LEFT JOIN FETCH d.equipoCatalogo " +
           "LEFT JOIN FETCH d.equipoVariante " +
           "LEFT JOIN FETCH d.estancia " +
           "WHERE d.user.id = :userId AND d.estancia.id = :estanciaId AND d.activo = true")
    List<DispositivoUsuario> findByUserIdAndEstanciaId(@Param("userId") Long userId, @Param("estanciaId") Long estanciaId);

}
