package com.ariana.streamingapi.mapper;

import com.ariana.streamingapi.dto.SuscripcionResponse;
import com.ariana.streamingapi.model.Suscripcion;
import org.springframework.stereotype.Component;

@Component
public class SuscripcionMapper {

    public SuscripcionResponse toResponse(Suscripcion suscripcion) {
        return new SuscripcionResponse(
                suscripcion.getId(),
                suscripcion.getUsuario().getId(),
                suscripcion.getUsuario().getEmail(),
                suscripcion.getPlan().getId(),
                suscripcion.getPlan().getNombre(),
                suscripcion.getFechaInicio(),
                suscripcion.getFechaFin(),
                suscripcion.getEstado(),
                suscripcion.getFechaCancelacion()
        );
    }
}