package com.ariana.streamingapi.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;



@Entity @Table(name="planes") @Getter @Setter @NoArgsConstructor  @AllArgsConstructor @Builder
public class Plan {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable= false, unique = true, length=50)
    private String nombre;

    @Column(name="precio_mensual", nullable=false, precision = 6, scale=2)
    private BigDecimal precioMensual;

    @Enumerated(EnumType.STRING)
    @Column (name="calidad_maxima", nullable=false, length = 10)
    private CalidadMaxima calidadMaxima;

    @Column(name= "pantallas_simultaneas", nullable = false)
    private Integer pantallasSimultaneas;

    @Column(nullable=false)
    private boolean activo;

}

//poner entity te mapea a una tabla, table name es el nombre explícito de la tabla en la base de datos
//getter, setter, noargs, allargs, builder es de lombok que ahorra código
