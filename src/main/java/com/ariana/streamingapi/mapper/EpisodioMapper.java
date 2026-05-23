package com.ariana.streamingapi.mapper;

import com.ariana.streamingapi.dto.EpisodioRequest;
import com.ariana.streamingapi.dto.EpisodioResponse;
import com.ariana.streamingapi.model.Episodio;
import org.springframework.stereotype.Component;

@Component
public class EpisodioMapper {

    public Episodio toEntity(EpisodioRequest request) {
        return Episodio.builder()
                .temporada(request.temporada())
                .numero(request.numero())
                .titulo(request.titulo())
                .duracionMinutos(request.duracionMinutos())
                .build();
    }

    public EpisodioResponse toResponse(Episodio episodio) {
        return new EpisodioResponse(
                episodio.getId(),
                episodio.getSerie().getId(),
                episodio.getSerie().getTitulo(),
                episodio.getTemporada(),
                episodio.getNumero(),
                episodio.getTitulo(),
                episodio.getDuracionMinutos()
        );
    }
}
