package com.ariana.streamingapi.controller;

import com.ariana.streamingapi.dto.PlanRequest;
import com.ariana.streamingapi.dto.PlanResponse;
import com.ariana.streamingapi.mapper.PlanMapper;
import com.ariana.streamingapi.model.Plan;
import com.ariana.streamingapi.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/planes")
public class PlanController {

    private final PlanService planService;
    private final PlanMapper planMapper;

    public PlanController(PlanService planService, PlanMapper planMapper) {
        this.planService = planService;
        this.planMapper = planMapper;
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> listarTodos() {
        List<PlanResponse> respuesta = planService.listarTodos().stream()
                .map(planMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> obtenerPorId(@PathVariable Long id) {
        Plan plan = planService.obtenerPorId(id);
        return ResponseEntity.ok(planMapper.toResponse(plan));
    }

    @PostMapping
    public ResponseEntity<PlanResponse> crear(@Valid @RequestBody PlanRequest request) {
        Plan nuevo = planService.crear(planMapper.toEntity(request));
        URI location = URI.create("/api/v1/planes/" + nuevo.getId());
        return ResponseEntity.created(location).body(planMapper.toResponse(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlanRequest request) {
        Plan actualizado = planService.actualizar(id, planMapper.toEntity(request));
        return ResponseEntity.ok(planMapper.toResponse(actualizado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlanResponse> actualizarParcial(
            @PathVariable Long id,
            @RequestBody PlanRequest request) {
        Plan actualizado = planService.actualizarParcial(id, planMapper.toEntity(request));
        return ResponseEntity.ok(planMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        planService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PlanResponse>> listarActivos() {
        List<PlanResponse> respuesta = planService.listarActivos().stream()
                .map(planMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PlanResponse>> buscarPorRangoPrecio(
            @RequestParam BigDecimal precioMin,
            @RequestParam BigDecimal precioMax) {
        List<PlanResponse> respuesta = planService.buscarPorRangoPrecio(precioMin, precioMax).stream()
                .map(planMapper::toResponse)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
}
