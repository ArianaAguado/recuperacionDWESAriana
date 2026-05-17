package com.ariana.streamingapi.service;

import com.ariana.streamingapi.exception.RecursoNoEncontradoException;
import com.ariana.streamingapi.model.Genero;
import com.ariana.streamingapi.model.Serie;
import com.ariana.streamingapi.repository.SerieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class SerieService {

    private final SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public List<Serie> listarTodas() {
        return serieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Serie obtenerPorId(Long id) {
        return serieRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Serie no encontrada con id " + id));
    }

    public Serie crear(Serie serie) {
        return serieRepository.save(serie);
    }

    public Serie actualizar(Long id, Serie datosActualizados) {
        Serie serie = obtenerPorId(id);
        serie.setTitulo(datosActualizados.getTitulo());
        serie.setSinopsis(datosActualizados.getSinopsis());
        serie.setGenero(datosActualizados.getGenero());
        serie.setAnyoEstreno(datosActualizados.getAnyoEstreno());
        serie.setValoracionMedia(datosActualizados.getValoracionMedia());
        return serieRepository.save(serie);
    }

    public Serie actualizarParcial(Long id, Serie datosParciales) {
        Serie serie = obtenerPorId(id);
        if (datosParciales.getTitulo() != null) {
            serie.setTitulo(datosParciales.getTitulo());
        }
        if (datosParciales.getSinopsis() != null) {
            serie.setSinopsis(datosParciales.getSinopsis());
        }
        if (datosParciales.getGenero() != null) {
            serie.setGenero(datosParciales.getGenero());
        }
        if (datosParciales.getAnyoEstreno() != null) {
            serie.setAnyoEstreno(datosParciales.getAnyoEstreno());
        }
        if (datosParciales.getValoracionMedia() != null) {
            serie.setValoracionMedia(datosParciales.getValoracionMedia());
        }
        return serieRepository.save(serie);
    }

    public void eliminar(Long id) {
        Serie serie = obtenerPorId(id);
        serieRepository.delete(serie);
    }

    @Transactional(readOnly = true)
    public List<Serie> buscarPorTitulo(String titulo) {
        return serieRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Transactional(readOnly = true)
    public List<Serie> buscarPorGenero(Genero genero) {
        return serieRepository.findByGenero(genero);
    }
}
