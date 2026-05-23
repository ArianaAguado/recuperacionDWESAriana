package com.ariana.streamingapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EpisodioRequest(

        @NotNull(message = "El id de la serie es obligatorio")
        Long serieId,

        @NotNull(message = "La temporada es obligatoria")
        @Positive(message = "La temporada debe ser un numero positivo")
        Integer temporada,

        @NotNull(message = "El numero de episodio es obligatorio")
        @Positive(message = "El numero de episodio debe ser positivo")
        Integer numero,

        @NotBlank(message = "El titulo del episodio es obligatorio")
        @Size(max = 200, message = "El titulo no puede tener mas de 200 caracteres")
        String titulo,

        @NotNull(message = "La duracion en minutos es obligatoria")
        @Positive(message = "La duracion debe ser positiva")
        Integer duracionMinutos
) {}