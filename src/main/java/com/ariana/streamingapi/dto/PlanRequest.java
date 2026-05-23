package com.ariana.streamingapi.dto;

import com.ariana.streamingapi.model.CalidadMaxima;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PlanRequest(
        @NotBlank(message = "El nombre del plan es obligatorio")
        @Size(max = 50, message = "El nombre no puede tener mas de 50 caracteres")
        String nombre,

        @NotNull(message = "El precio mensual es obligatorio")
        @Positive(message = "El precio mensual debe ser mayor que 0")
        BigDecimal precioMensual,

        @NotNull(message = "La calidad maxima es obligatoria")
        CalidadMaxima calidadMaxima,

        @NotNull(message = "El numero de pantallas simultaneas es obligatorio")
        @Positive(message = "Las pantallas simultaneas deben ser un numero positivo")
        Integer pantallasSimultaneas,

        boolean activo
        )
{}
