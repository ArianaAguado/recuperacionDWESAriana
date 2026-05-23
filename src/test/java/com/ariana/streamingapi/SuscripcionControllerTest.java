package com.ariana.streamingapi;

import com.ariana.streamingapi.controller.SuscripcionController;
import com.ariana.streamingapi.mapper.SuscripcionMapper;
import com.ariana.streamingapi.model.*;
import com.ariana.streamingapi.service.SuscripcionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuscripcionController.class)
class SuscripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SuscripcionService suscripcionService;

    @MockitoBean
    private SuscripcionMapper suscripcionMapper;

    @Test
    void postSuscribir_devuelveSuscripcionCon201() throws Exception {
        Usuario usuario = Usuario.builder().id(1L).email("test@example.com").nombre("Test").activo(true).build();
        Plan plan = Plan.builder().Id(1L).nombre("Basico").precioMensual(new BigDecimal("7.99"))
                .calidadMaxima(CalidadMaxima.SD).pantallasSimultaneas(1).activo(true).build();

        Suscripcion suscripcion = Suscripcion.builder()
                .id(1L)
                .usuario(usuario)
                .plan(plan)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusDays(30))
                .estado(EstadoSuscripcion.ACTIVA)
                .build();

        when(suscripcionService.suscribir(anyLong(), anyLong())).thenReturn(suscripcion);
        when(suscripcionMapper.toResponse(suscripcion)).thenReturn(
                new com.ariana.streamingapi.dto.SuscripcionResponse(
                        1L, 1L, "test@example.com", 1L, "Basico",
                        LocalDate.now(), LocalDate.now().plusDays(30),
                        EstadoSuscripcion.ACTIVA, null
                )
        );

        String body = objectMapper.writeValueAsString(Map.of(
                "usuarioId", 1,
                "planId", 1
        ));

        mockMvc.perform(post("/api/v1/suscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("ACTIVA"));
    }
}
