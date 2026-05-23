package com.ariana.streamingapi.dto;

import com.ariana.streamingapi.model.Genero;

public record SerieResponse(
        Long id,
        String titulo,
        String sinopsis,
        Genero genero,
        Integer anyoEstreno,
        Double valoracionMedia
) {}