package com.ariana.streamingapi.dto;

import com.ariana.streamingapi.model.EstadoSuscripcion;

import java.time.LocalDate;

public record SuscripcionUpdateRequest(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoSuscripcion estado
) {}