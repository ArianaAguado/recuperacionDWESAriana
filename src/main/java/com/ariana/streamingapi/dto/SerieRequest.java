package com.ariana.streamingapi.dto;

import com.ariana.streamingapi.model.Genero;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SerieRequest(

        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 150, message = "El titulo no puede tener mas de 150 caracteres")
        String titulo,

        @Size(max = 2000, message = "La sinopsis no puede tener mas de 2000 caracteres")
        String sinopsis,

        @NotNull(message = "El genero es obligatorio")
        Genero genero,

        @NotNull(message = "El año de estreno es obligatorio")
        @Min(value = 1900, message = "El año de estreno debe ser posterior a 1900")
        Integer anyoEstreno,

        Double valoracionMedia
) {}