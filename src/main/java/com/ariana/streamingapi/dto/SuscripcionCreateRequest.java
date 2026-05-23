package com.ariana.streamingapi.dto;

import jakarta.validation.constraints.NotNull;

public record SuscripcionCreateRequest(

        @NotNull(message = "El id de usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "El id del plan es obligatorio")
        Long planId
) {}