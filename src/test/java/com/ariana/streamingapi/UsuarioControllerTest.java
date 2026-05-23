package com.ariana.streamingapi;

import com.ariana.streamingapi.controller.UsuarioController;
import com.ariana.streamingapi.mapper.UsuarioMapper;
import com.ariana.streamingapi.model.Usuario;
import com.ariana.streamingapi.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioMapper usuarioMapper;

    @Test
    void postCrearUsuario_devuelveUsuarioCon201() throws Exception {
        Usuario nuevo = Usuario.builder()
                .id(1L)
                .email("test@example.com")
                .nombre("Test User")
                .fechaRegistro(LocalDateTime.now())
                .activo(true)
                .build();

        when(usuarioMapper.toEntity(any())).thenReturn(nuevo);
        when(usuarioService.crear(any(Usuario.class))).thenReturn(nuevo);
        when(usuarioMapper.toResponse(nuevo)).thenReturn(
                new com.ariana.streamingapi.dto.UsuarioResponse(
                        1L, "test@example.com", "Test User",
                        LocalDateTime.now(), true
                )
        );

        String body = objectMapper.writeValueAsString(Map.of(
                "email", "test@example.com",
                "nombre", "Test User"
        ));

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nombre").value("Test User"));
    }
}