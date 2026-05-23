package com.ariana.streamingapi.mapper;

import com.ariana.streamingapi.dto.PlanRequest;
import com.ariana.streamingapi.dto.PlanResponse;
import com.ariana.streamingapi.model.Plan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    public Plan toEntity(PlanRequest request) {
        return Plan.builder()
                .nombre(request.nombre())
                .precioMensual(request.precioMensual())
                .calidadMaxima(request.calidadMaxima())
                .pantallasSimultaneas(request.pantallasSimultaneas())
                .activo(request.activo())
                .build();
    }

    public PlanResponse toResponse(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getNombre(),
                plan.getPrecioMensual(),
                plan.getCalidadMaxima(),
                plan.getPantallasSimultaneas(),
                plan.isActivo()
        );
    }
}