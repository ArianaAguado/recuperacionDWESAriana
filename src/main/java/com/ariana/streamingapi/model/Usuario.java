package com.ariana.streamingapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Table(name="usuarios") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true, length = 100)
    private String email;

    @Column(nullable=false, length = 100)
    private String nombre;

    @Column(name="fecha_registro", nullable=false)
    private LocalDateTime fechaRegistro;

    @Column(nullable=false)
    private boolean activo;

}
