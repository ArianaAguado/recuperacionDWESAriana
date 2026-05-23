package com.ariana.streamingapi.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String email,
        String nombre,
        LocalDateTime fechaRegistro,
        boolean activo
) {}