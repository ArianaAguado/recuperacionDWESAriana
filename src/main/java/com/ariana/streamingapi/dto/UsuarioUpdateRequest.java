package com.ariana.streamingapi.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(

        @Email(message = "El email debe tener un formato valido")
        @Size(max = 100, message = "El email no puede tener mas de 100 caracteres")
        String email,

        @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
        String nombre,

        Boolean activo
) {}