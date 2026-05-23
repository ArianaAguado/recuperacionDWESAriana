package com.ariana.streamingapi.dto;

public record EpisodioResponse(
        Long id,
        Long serieId,
        String serieTitulo,
        Integer temporada,
        Integer numero,
        String titulo,
        Integer duracionMinutos
) {}