package com.ariana.streamingapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="episodios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Episodio {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "serie_id", nullable = false)
        private Serie serie;

        @Column(nullable = false)
        private Integer temporada;

        @Column(nullable = false)
        private Integer numero;

        @Column(nullable = false, length = 200)
        private String titulo;

        @Column(name = "duracion_minutos", nullable = false)
        private Integer duracionMinutos;
    }

