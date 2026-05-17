package com.ariana.streamingapi.repository;

import com.ariana.streamingapi.model.Genero;
import com.ariana.streamingapi.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    List<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findByGenero(Genero genero);

    List<Serie> findByAnyoEstreno(Integer anyoEstreno);
}