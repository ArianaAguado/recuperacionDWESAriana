package com.ariana.streamingapi.service;

import com.ariana.streamingapi.exception.RecursoDuplicadoException;
import com.ariana.streamingapi.exception.RecursoNoEncontradoException;
import com.ariana.streamingapi.model.Episodio;
import com.ariana.streamingapi.model.Serie;
import com.ariana.streamingapi.repository.EpisodioRepository;
import com.ariana.streamingapi.repository.SerieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class EpisodioService {
    private final EpisodioRepository episodioRepository;
    private final SerieRepository serieRepository;

    public EpisodioService(EpisodioRepository episodioRepository,
                           SerieRepository serieRepository) {
        this.episodioRepository = episodioRepository;
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public List<Episodio> listarTodos() {
        return episodioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Episodio obtenerPorId(Long id) {
        return episodioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Episodio no encontrado con id " + id));
    }

    public Episodio crear(Long serieId, Episodio episodio) {
        // Regla 1: la serie debe existir
        Serie serie = serieRepository.findById(serieId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Serie no encontrada con id " + serieId));

        // Regla 2: no puede haber otro episodio con misma temporada y número en esa serie
        if (episodioRepository.existsBySerieIdAndTemporadaAndNumero(
                serieId, episodio.getTemporada(), episodio.getNumero())) {
            throw new RecursoDuplicadoException(
                    "Ya existe el episodio " + episodio.getNumero() +
                            " de la temporada " + episodio.getTemporada() +
                            " en la serie con id " + serieId);
        }

        episodio.setSerie(serie);
        return episodioRepository.save(episodio);
    }

    public Episodio actualizar(Long id, Episodio datosActualizados) {
        Episodio episodio = obtenerPorId(id);
        episodio.setTemporada(datosActualizados.getTemporada());
        episodio.setNumero(datosActualizados.getNumero());
        episodio.setTitulo(datosActualizados.getTitulo());
        episodio.setDuracionMinutos(datosActualizados.getDuracionMinutos());
        return episodioRepository.save(episodio);
    }

    public Episodio actualizarParcial(Long id, Episodio datosParciales) {
        Episodio episodio = obtenerPorId(id);
        if (datosParciales.getTemporada() != null) {
            episodio.setTemporada(datosParciales.getTemporada());
        }
        if (datosParciales.getNumero() != null) {
            episodio.setNumero(datosParciales.getNumero());
        }
        if (datosParciales.getTitulo() != null) {
            episodio.setTitulo(datosParciales.getTitulo());
        }
        if (datosParciales.getDuracionMinutos() != null) {
            episodio.setDuracionMinutos(datosParciales.getDuracionMinutos());
        }
        return episodioRepository.save(episodio);
    }

    public void eliminar(Long id) {
        Episodio episodio = obtenerPorId(id);
        episodioRepository.delete(episodio);
    }

    @Transactional(readOnly = true)
    public List<Episodio> buscarPorSerie(Long serieId) {
        return episodioRepository.findBySerieId(serieId);
    }

    @Transactional(readOnly = true)
    public List<Episodio> buscarPorSerieYTemporada(Long serieId, Integer temporada) {
        return episodioRepository.findBySerieIdAndTemporada(serieId, temporada);
    }
}
