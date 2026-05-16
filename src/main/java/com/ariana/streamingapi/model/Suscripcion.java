package com.ariana.streamingapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "suscripciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suscripcion {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "plan_id", nullable = false)
        private Plan plan;

        @Column(name = "fecha_inicio", nullable = false)
        private LocalDate fechaInicio;

        @Column(name = "fecha_fin", nullable = false)
        private LocalDate fechaFin;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private EstadoSuscripcion estado;

        @Column(name = "fecha_cancelacion")
        private LocalDate fechaCancelacion;

}
