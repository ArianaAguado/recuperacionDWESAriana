package com.ariana.streamingapi.repository;

import com.ariana.streamingapi.model.EstadoSuscripcion;
import com.ariana.streamingapi.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    List<Suscripcion> findByUsuarioId(Long usuarioId);

    List<Suscripcion> findByEstado(EstadoSuscripcion estado);

    Optional<Suscripcion> findByUsuarioIdAndEstado(Long usuarioId, EstadoSuscripcion estado);

    boolean existsByUsuarioIdAndEstado(Long usuarioId, EstadoSuscripcion estado);
}

//optional era para buscar por un campo único, por si no existe no devolver nulos
//list es para cuadno puede haber varios resultados y si no, no devuelve null sino una lista vacía