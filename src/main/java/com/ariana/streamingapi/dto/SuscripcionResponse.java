package com.ariana.streamingapi.dto;

import com.ariana.streamingapi.model.EstadoSuscripcion;

import java.time.LocalDate;

public record SuscripcionResponse(
        Long id,
        Long usuarioId,
        String usuarioEmail,
        Long planId,
        String planNombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoSuscripcion estado,
        LocalDate fechaCancelacion
) {}