package com.ariana.streamingapi.repository;

import com.ariana.streamingapi.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long>{

    Optional<Plan> findByNombre(String nombre);
    List<Plan> findByActivoTrue();
    List<Plan> findByPrecioMensualBetween(BigDecimal min, BigDecimal max);
    boolean existsByNombre(String nombre);

}
