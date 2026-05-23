package com.ariana.streamingapi;

import com.ariana.streamingapi.controller.EpisodioController;
import com.ariana.streamingapi.exception.RecursoNoEncontradoException;
import com.ariana.streamingapi.mapper.EpisodioMapper;
import com.ariana.streamingapi.service.EpisodioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EpisodioController.class)
class EpisodioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EpisodioService episodioService;

    @MockitoBean
    private EpisodioMapper episodioMapper;

    @Test
    void getEpisodioInexistente_devuelve404() throws Exception {
        when(episodioService.obtenerPorId(999L))
                .thenThrow(new RecursoNoEncontradoException("Episodio no encontrado con id 999"));

        mockMvc.perform(get("/api/v1/episodios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
