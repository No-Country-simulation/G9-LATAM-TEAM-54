package com.energiai.backend.repository;

import com.energiai.backend.model.DispositivoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispositivoUsuarioRepository extends JpaRepository<DispositivoUsuario, Long> {

    @Query("SELECT d FROM DispositivoUsuario d " +
           "LEFT JOIN FETCH d.equipoCatalogo " +
           "LEFT JOIN FETCH d.equipoVariante " +
           "LEFT JOIN FETCH d.estancia " +
           "WHERE d.user.id = :userId")
    List<DispositivoUsuario> findByUserId(@Param("userId") Long userId);

    @Query("SELECT d FROM DispositivoUsuario d " +
           "LEFT JOIN FETCH d.equipoCatalogo " +
           "LEFT JOIN FETCH d.equipoVariante " +
           "LEFT JOIN FETCH d.estancia " +
           "WHERE d.user.id = :userId AND d.estancia.id = :estanciaId")
    List<DispositivoUsuario> findByUserIdAndEstanciaId(@Param("userId") Long userId, @Param("estanciaId") Long estanciaId);

}
