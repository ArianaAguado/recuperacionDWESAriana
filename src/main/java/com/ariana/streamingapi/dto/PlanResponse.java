package com.ariana.streamingapi.dto;

import com.ariana.streamingapi.model.CalidadMaxima;

import java.math.BigDecimal;

public record PlanResponse(
        Long id,
        String nombre,
        BigDecimal precioMensual,
        CalidadMaxima calidadMaxima,
        Integer pantallasSimultaneas,
        boolean activo
) {}