package com.ariana.streamingapi;

import com.ariana.streamingapi.controller.PlanController;
import com.ariana.streamingapi.mapper.PlanMapper;
import com.ariana.streamingapi.model.CalidadMaxima;
import com.ariana.streamingapi.model.Plan;
import com.ariana.streamingapi.service.PlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanController.class)
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlanService planService;

    @MockitoBean
    private PlanMapper planMapper;

    @Test
    void getListarPlanes_devuelveListaCon200() throws Exception {
        Plan plan = Plan.builder()
                .Id(1L)
                .nombre("Basico")
                .precioMensual(new BigDecimal("7.99"))
                .calidadMaxima(CalidadMaxima.SD)
                .pantallasSimultaneas(1)
                .activo(true)
                .build();

        when(planService.listarTodos()).thenReturn(List.of(plan));
        when(planMapper.toResponse(plan)).thenReturn(
                new com.ariana.streamingapi.dto.PlanResponse(
                        1L, "Basico", new BigDecimal("7.99"),
                        CalidadMaxima.SD, 1, true
                )
        );

        mockMvc.perform(get("/api/v1/planes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Basico"));
    }
}
