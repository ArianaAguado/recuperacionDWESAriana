package com.ariana.streamingapi.controller;

import com.ariana.streamingapi.dto.SuscripcionCreateRequest;
import com.ariana.streamingapi.dto.SuscripcionUpdateRequest;
import com.ariana.streamingapi.dto.SuscripcionResponse;
import com.ariana.streamingapi.mapper.SuscripcionMapper;
import com.ariana.streamingapi.model.EstadoSuscripcion;
import com.ariana.streamingapi.model.Suscripcion;
import com.ariana.streamingapi.service.SuscripcionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;
    private final SuscripcionMapper suscripcionMapper;

    public SuscripcionController(SuscripcionService suscripcionService,
                                 SuscripcionMapper suscripcionMapper) {
        this.suscripcionService = suscripcionService;
        this.suscripcionMapper = suscripcionMapper;
    }

    @GetMapping
    public ResponseEntity<List<SuscripcionResponse>> listarTodas() {
        List<SuscripcionResponse> respuesta = suscripcionService.listarTodas().stream()
                .map(suscripcionMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuscripcionResponse> obtenerPorId(@PathVariable Long id) {
        Suscripcion suscripcion = suscripcionService.obtenerPorId(id);
        return ResponseEntity.ok(suscripcionMapper.toResponse(suscripcion));
    }

    @PostMapping
    public ResponseEntity<SuscripcionResponse> suscribir(@Valid @RequestBody SuscripcionCreateRequest request) {
        Suscripcion nueva = suscripcionService.suscribir(request.usuarioId(), request.planId());
        URI location = URI.create("/api/v1/suscripciones/" + nueva.getId());
        return ResponseEntity.created(location).body(suscripcionMapper.toResponse(nueva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuscripcionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SuscripcionUpdateRequest request) {
        Suscripcion datos = Suscripcion.builder()
                .fechaInicio(request.fechaInicio())
                .fechaFin(request.fechaFin())
                .estado(request.estado())
                .build();
        Suscripcion actualizada = suscripcionService.actualizar(id, datos);
        return ResponseEntity.ok(suscripcionMapper.toResponse(actualizada));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuscripcionResponse> actualizarParcial(
            @PathVariable Long id,
            @RequestBody SuscripcionUpdateRequest request) {
        Suscripcion datos = Suscripcion.builder()
                .fechaInicio(request.fechaInicio())
                .fechaFin(request.fechaFin())
                .estado(request.estado())
                .build();
        Suscripcion actualizada = suscripcionService.actualizarParcial(id, datos);
        return ResponseEntity.ok(suscripcionMapper.toResponse(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        suscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<SuscripcionResponse> cancelar(@PathVariable Long id) {
        Suscripcion cancelada = suscripcionService.cancelar(id);
        return ResponseEntity.ok(suscripcionMapper.toResponse(cancelada));
    }

    @PostMapping("/usuario/{usuarioId}/renovar")
    public ResponseEntity<SuscripcionResponse> renovar(@PathVariable Long usuarioId) {
        Suscripcion nueva = suscripcionService.renovar(usuarioId);
        URI location = URI.create("/api/v1/suscripciones/" + nueva.getId());
        return ResponseEntity.created(location).body(suscripcionMapper.toResponse(nueva));
    }

    @PostMapping("/marcar-caducadas")
    public ResponseEntity<Map<String, Integer>> marcarCaducadas() {
        int caducadas = suscripcionService.marcarCaducadasLasVencidas();
        return ResponseEntity.ok(Map.of("caducadas", caducadas));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SuscripcionResponse>> buscarPorUsuario(@PathVariable Long usuarioId) {
        List<SuscripcionResponse> respuesta = suscripcionService.buscarPorUsuario(usuarioId).stream()
                .map(suscripcionMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SuscripcionResponse>> buscarPorEstado(@PathVariable EstadoSuscripcion estado) {
        List<SuscripcionResponse> respuesta = suscripcionService.buscarPorEstado(estado).stream()
                .map(suscripcionMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
}