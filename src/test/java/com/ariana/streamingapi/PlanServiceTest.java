package com.ariana.streamingapi;

import com.ariana.streamingapi.exception.RecursoDuplicadoException;
import com.ariana.streamingapi.exception.RecursoNoEncontradoException;
import com.ariana.streamingapi.model.CalidadMaxima;
import com.ariana.streamingapi.model.Plan;
import com.ariana.streamingapi.repository.PlanRepository;
import com.ariana.streamingapi.service.PlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

    private Plan planBasico;

    @BeforeEach
    void setUp() {
        planBasico = Plan.builder()
                .Id(1L)
                .nombre("Basico")
                .precioMensual(new BigDecimal("7.99"))
                .calidadMaxima(CalidadMaxima.SD)
                .pantallasSimultaneas(1)
                .activo(true)
                .build();
    }

    @Test
    void obtenerPorId_existente_devuelvePlan() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(planBasico));

        Plan resultado = planService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("Basico", resultado.getNombre());
        verify(planRepository).findById(1L);
    }

    @Test
    void obtenerPorId_inexistente_lanzaExcepcion() {
        when(planRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> planService.obtenerPorId(99L));
    }

    @Test
    void crear_nombreUnico_guardaPlan() {
        when(planRepository.existsByNombre("Basico")).thenReturn(false);
        when(planRepository.save(any(Plan.class))).thenReturn(planBasico);

        Plan resultado = planService.crear(planBasico);

        assertNotNull(resultado);
        assertEquals("Basico", resultado.getNombre());
        verify(planRepository).save(planBasico);
    }

    @Test
    void crear_nombreDuplicado_lanzaExcepcion() {
        when(planRepository.existsByNombre("Basico")).thenReturn(true);

        assertThrows(RecursoDuplicadoException.class,
                () -> planService.crear(planBasico));

        verify(planRepository, never()).save(any(Plan.class));
    }
}
