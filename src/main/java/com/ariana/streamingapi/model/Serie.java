package com.ariana.streamingapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity @Table(name="series") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Serie {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length = 150)
    private String titulo;

    @Column(length = 2000)
    private String sinopsis;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length = 20)
    private Genero genero;

    @Column(name="anyo_estreno", nullable=false)
    private Integer anyoEstreno;

    @Column(name="valoracion_media")
    private Double valoracionMedia;

}
