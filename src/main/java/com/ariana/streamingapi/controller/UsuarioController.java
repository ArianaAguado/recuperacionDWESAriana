package com.ariana.streamingapi.controller;

import com.ariana.streamingapi.dto.UsuarioCreateRequest;
import com.ariana.streamingapi.dto.UsuarioUpdateRequest;
import com.ariana.streamingapi.dto.UsuarioResponse;
import com.ariana.streamingapi.mapper.UsuarioMapper;
import com.ariana.streamingapi.model.Usuario;
import com.ariana.streamingapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> respuesta = usuarioService.listarTodos().stream()
                .map(usuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioCreateRequest request) {
        Usuario nuevo = usuarioService.crear(usuarioMapper.toEntity(request));
        URI location = URI.create("/api/v1/usuarios/" + nuevo.getId());
        return ResponseEntity.created(location).body(usuarioMapper.toResponse(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest request) {
        Usuario datos = Usuario.builder()
                .email(request.email())
                .nombre(request.nombre())
                .activo(request.activo() != null ? request.activo() : true)
                .build();
        Usuario actualizado = usuarioService.actualizar(id, datos);
        return ResponseEntity.ok(usuarioMapper.toResponse(actualizado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarParcial(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateRequest request) {
        Usuario datos = Usuario.builder()
                .email(request.email())
                .nombre(request.nombre())
                .build();
        Usuario actualizado = usuarioService.actualizarParcial(id, datos);
        return ResponseEntity.ok(usuarioMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/email")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@RequestParam String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponse>> listarActivos() {
        List<UsuarioResponse> respuesta = usuarioService.listarActivos().stream()
                .map(usuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
}