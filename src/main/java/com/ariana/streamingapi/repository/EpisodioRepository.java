package com.ariana.streamingapi.repository;

import com.ariana.streamingapi.model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodioRepository extends JpaRepository<Episodio, Long> {

    List<Episodio> findBySerieId(Long serieId);

    List<Episodio> findBySerieIdAndTemporada(Long serieId, Integer temporada);

    boolean existsBySerieIdAndTemporadaAndNumero(Long serieId, Integer temporada, Integer numero);
}