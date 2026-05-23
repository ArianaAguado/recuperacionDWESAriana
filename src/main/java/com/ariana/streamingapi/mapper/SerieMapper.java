package com.ariana.streamingapi.mapper;

import com.ariana.streamingapi.dto.SerieRequest;
import com.ariana.streamingapi.dto.SerieResponse;
import com.ariana.streamingapi.model.Serie;
import org.springframework.stereotype.Component;

@Component
public class SerieMapper {

    public Serie toEntity(SerieRequest request) {
        return Serie.builder()
                .titulo(request.titulo())
                .sinopsis(request.sinopsis())
                .genero(request.genero())
                .anyoEstreno(request.anyoEstreno())
                .valoracionMedia(request.valoracionMedia())
                .build();
    }

    public SerieResponse toResponse(Serie serie) {
        return new SerieResponse(
                serie.getId(),
                serie.getTitulo(),
                serie.getSinopsis(),
                serie.getGenero(),
                serie.getAnyoEstreno(),
                serie.getValoracionMedia()
        );
    }
}
