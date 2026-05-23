package com.ariana.streamingapi;

import com.ariana.streamingapi.exception.OperacionNoPermitida;
import com.ariana.streamingapi.model.*;
import com.ariana.streamingapi.repository.PlanRepository;
import com.ariana.streamingapi.repository.SuscripcionRepository;
import com.ariana.streamingapi.repository.UsuarioRepository;
import com.ariana.streamingapi.service.SuscripcionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuscripcionServiceTest {

    @Mock
    private SuscripcionRepository suscripcionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private SuscripcionService suscripcionService;

    private Usuario usuario;
    private Plan plan;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .email("test@example.com")
                .nombre("Test")
                .activo(true)
                .build();

        plan = Plan.builder()
                .Id(1L)
                .nombre("Basico")
                .precioMensual(new BigDecimal("7.99"))
                .calidadMaxima(CalidadMaxima.SD)
                .pantallasSimultaneas(1)
                .activo(true)
                .build();
    }

    @Test
    void suscribir_usuarioYaTieneActiva_lanzaExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(suscripcionRepository.existsByUsuarioIdAndEstado(1L, EstadoSuscripcion.ACTIVA))
                .thenReturn(true);

        assertThrows(OperacionNoPermitida.class,
                () -> suscripcionService.suscribir(1L, 1L));

        verify(suscripcionRepository, never()).save(any(Suscripcion.class));
    }

    @Test
    void cancelar_suscripcionYaCancelada_lanzaExcepcion() {
        Suscripcion cancelada = Suscripcion.builder()
                .id(1L)
                .usuario(usuario)
                .plan(plan)
                .fechaInicio(LocalDate.now().minusDays(30))
                .fechaFin(LocalDate.now())
                .estado(EstadoSuscripcion.CANCELADA)
                .build();

        when(suscripcionRepository.findById(1L)).thenReturn(Optional.of(cancelada));

        assertThrows(OperacionNoPermitida.class,
                () -> suscripcionService.cancelar(1L));

        verify(suscripcionRepository, never()).save(any(Suscripcion.class));
    }
}