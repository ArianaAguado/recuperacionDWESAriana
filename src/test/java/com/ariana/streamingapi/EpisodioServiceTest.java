package com.ariana.streamingapi;

import com.ariana.streamingapi.exception.RecursoDuplicadoException;
import com.ariana.streamingapi.model.Episodio;
import com.ariana.streamingapi.model.Genero;
import com.ariana.streamingapi.model.Serie;
import com.ariana.streamingapi.repository.EpisodioRepository;
import com.ariana.streamingapi.repository.SerieRepository;
import com.ariana.streamingapi.service.EpisodioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpisodioServiceTest {

    @Mock
    private EpisodioRepository episodioRepository;

    @Mock
    private SerieRepository serieRepository;

    @InjectMocks
    private EpisodioService episodioService;

    private Serie serie;
    private Episodio episodio;

    @BeforeEach
    void setUp() {
        serie = Serie.builder()
                .id(1L)
                .titulo("Stranger Things")
                .genero(Genero.SCIFI)
                .anyoEstreno(2016)
                .build();

        episodio = Episodio.builder()
                .temporada(1)
                .numero(1)
                .titulo("Chapter One")
                .duracionMinutos(48)
                .build();
    }

    @Test
    void crear_episodioDuplicadoEnTemporada_lanzaExcepcion() {
        when(serieRepository.findById(1L)).thenReturn(Optional.of(serie));
        when(episodioRepository.existsBySerieIdAndTemporadaAndNumero(1L, 1, 1))
                .thenReturn(true);

        assertThrows(RecursoDuplicadoException.class,
                () -> episodioService.crear(1L, episodio));

        verify(episodioRepository, never()).save(any(Episodio.class));
    }
}
