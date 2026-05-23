package com.ariana.streamingapi.controller;

import com.ariana.streamingapi.dto.SerieRequest;
import com.ariana.streamingapi.dto.SerieResponse;
import com.ariana.streamingapi.mapper.SerieMapper;
import com.ariana.streamingapi.model.Genero;
import com.ariana.streamingapi.model.Serie;
import com.ariana.streamingapi.service.SerieService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/series")
public class SerieController {

    private final SerieService serieService;
    private final SerieMapper serieMapper;

    public SerieController(SerieService serieService, SerieMapper serieMapper) {
        this.serieService = serieService;
        this.serieMapper = serieMapper;
    }

    @GetMapping
    public ResponseEntity<List<SerieResponse>> listarTodas() {
        List<SerieResponse> respuesta = serieService.listarTodas().stream()
                .map(serieMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SerieResponse> obtenerPorId(@PathVariable Long id) {
        Serie serie = serieService.obtenerPorId(id);
        return ResponseEntity.ok(serieMapper.toResponse(serie));
    }

    @PostMapping
    public ResponseEntity<SerieResponse> crear(@Valid @RequestBody SerieRequest request) {
        Serie nueva = serieService.crear(serieMapper.toEntity(request));
        URI location = URI.create("/api/v1/series/" + nueva.getId());
        return ResponseEntity.created(location).body(serieMapper.toResponse(nueva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SerieResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SerieRequest request) {
        Serie actualizada = serieService.actualizar(id, serieMapper.toEntity(request));
        return ResponseEntity.ok(serieMapper.toResponse(actualizada));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SerieResponse> actualizarParcial(
            @PathVariable Long id,
            @RequestBody SerieRequest request) {
        Serie actualizada = serieService.actualizarParcial(id, serieMapper.toEntity(request));
        return ResponseEntity.ok(serieMapper.toResponse(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        serieService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<SerieResponse>> buscarPorTitulo(@RequestParam String titulo) {
        List<SerieResponse> respuesta = serieService.buscarPorTitulo(titulo).stream()
                .map(serieMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<SerieResponse>> buscarPorGenero(@PathVariable Genero genero) {
        List<SerieResponse> respuesta = serieService.buscarPorGenero(genero).stream()
                .map(serieMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
}
