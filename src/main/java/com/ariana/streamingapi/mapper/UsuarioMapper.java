package com.ariana.streamingapi.mapper;


import com.ariana.streamingapi.dto.UsuarioCreateRequest;
import com.ariana.streamingapi.dto.UsuarioResponse;
import com.ariana.streamingapi.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioCreateRequest request) {
        return Usuario.builder()
                .email(request.email())
                .nombre(request.nombre())
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getFechaRegistro(),
                usuario.isActivo()
        );
    }
}