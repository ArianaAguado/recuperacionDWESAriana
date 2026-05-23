package com.ariana.streamingapi.controller;

import com.ariana.streamingapi.dto.EpisodioRequest;
import com.ariana.streamingapi.dto.EpisodioResponse;
import com.ariana.streamingapi.mapper.EpisodioMapper;
import com.ariana.streamingapi.model.Episodio;
import com.ariana.streamingapi.service.EpisodioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/episodios")
public class EpisodioController {

    private final EpisodioService episodioService;
    private final EpisodioMapper episodioMapper;

    public EpisodioController(EpisodioService episodioService, EpisodioMapper episodioMapper) {
        this.episodioService = episodioService;
        this.episodioMapper = episodioMapper;
    }

    @GetMapping
    public ResponseEntity<List<EpisodioResponse>> listarTodos() {
        List<EpisodioResponse> respuesta = episodioService.listarTodos().stream()
                .map(episodioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpisodioResponse> obtenerPorId(@PathVariable Long id) {
        Episodio episodio = episodioService.obtenerPorId(id);
        return ResponseEntity.ok(episodioMapper.toResponse(episodio));
    }

    @PostMapping
    public ResponseEntity<EpisodioResponse> crear(@Valid @RequestBody EpisodioRequest request) {
        Episodio nuevo = episodioService.crear(request.serieId(), episodioMapper.toEntity(request));
        URI location = URI.create("/api/v1/episodios/" + nuevo.getId());
        return ResponseEntity.created(location).body(episodioMapper.toResponse(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpisodioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EpisodioRequest request) {
        Episodio actualizado = episodioService.actualizar(id, episodioMapper.toEntity(request));
        return ResponseEntity.ok(episodioMapper.toResponse(actualizado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EpisodioResponse> actualizarParcial(
            @PathVariable Long id,
            @RequestBody EpisodioRequest request) {
        Episodio actualizado = episodioService.actualizarParcial(id, episodioMapper.toEntity(request));
        return ResponseEntity.ok(episodioMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        episodioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/serie/{serieId}")
    public ResponseEntity<List<EpisodioResponse>> buscarPorSerie(@PathVariable Long serieId) {
        List<EpisodioResponse> respuesta = episodioService.buscarPorSerie(serieId).stream()
                .map(episodioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/serie/{serieId}/temporada/{temporada}")
    public ResponseEntity<List<EpisodioResponse>> buscarPorSerieYTemporada(
            @PathVariable Long serieId,
            @PathVariable Integer temporada) {
        List<EpisodioResponse> respuesta = episodioService.buscarPorSerieYTemporada(serieId, temporada).stream()
                .map(episodioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
}
