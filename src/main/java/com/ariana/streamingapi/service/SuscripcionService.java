package com.ariana.streamingapi.service;

import com.ariana.streamingapi.exception.OperacionNoPermitida;
import com.ariana.streamingapi.exception.RecursoNoEncontradoException;
import com.ariana.streamingapi.model.EstadoSuscripcion;
import com.ariana.streamingapi.model.Plan;
import com.ariana.streamingapi.model.Suscripcion;
import com.ariana.streamingapi.model.Usuario;
import com.ariana.streamingapi.repository.PlanRepository;
import com.ariana.streamingapi.repository.SuscripcionRepository;
import com.ariana.streamingapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service @Transactional
public class SuscripcionService {
    private static final int DIAS_DURACION_SUSCRIPCION = 30;

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanRepository planRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository,
                              UsuarioRepository usuarioRepository,
                              PlanRepository planRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public List<Suscripcion> listarTodas() {
        return suscripcionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Suscripcion obtenerPorId(Long id) {
        return suscripcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Suscripcion no encontrada con id " + id));
    }

    //suscribir un usuario a un plan
    public Suscripcion suscribir(Long usuarioId, Long planId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id " + usuarioId));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Plan no encontrado con id " + planId));

        if (!plan.isActivo()) {
            throw new OperacionNoPermitida(
                    "El plan con id " + planId + " no esta activo");
        }

        if (suscripcionRepository.existsByUsuarioIdAndEstado(usuarioId, EstadoSuscripcion.ACTIVA)) {
            throw new OperacionNoPermitida(
                    "El usuario con id " + usuarioId + " ya tiene una suscripcion activa");
        }

        LocalDate hoy = LocalDate.now();
        Suscripcion suscripcion = Suscripcion.builder()
                .usuario(usuario)
                .plan(plan)
                .fechaInicio(hoy)
                .fechaFin(hoy.plusDays(DIAS_DURACION_SUSCRIPCION))
                .estado(EstadoSuscripcion.ACTIVA)
                .build();

        return suscripcionRepository.save(suscripcion);
    }

    //cancelamos suscripcion existente
    public Suscripcion cancelar(Long id) {
        Suscripcion suscripcion = obtenerPorId(id);

        if (suscripcion.getEstado() != EstadoSuscripcion.ACTIVA) {
            throw new OperacionNoPermitida(
                    "Solo se pueden cancelar suscripciones en estado ACTIVA. " +
                            "Estado actual: " + suscripcion.getEstado());
        }

        suscripcion.setEstado(EstadoSuscripcion.CANCELADA);
        suscripcion.setFechaCancelacion(LocalDate.now());
        return suscripcionRepository.save(suscripcion);
    }


    //renovamos la suscripcion del usuario existiendo una suscripcion previa y caducada
    public Suscripcion renovar(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id " + usuarioId));

        Suscripcion ultima = suscripcionRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoSuscripcion.CADUCADA)
                .orElseThrow(() -> new OperacionNoPermitida(
                        "El usuario con id " + usuarioId +
                                " no tiene ninguna suscripcion CADUCADA para renovar"));

        if (suscripcionRepository.existsByUsuarioIdAndEstado(usuarioId, EstadoSuscripcion.ACTIVA)) {
            throw new OperacionNoPermitida(
                    "El usuario con id " + usuarioId + " ya tiene una suscripcion activa");
        }

        LocalDate hoy = LocalDate.now();
        Suscripcion nueva = Suscripcion.builder()
                .usuario(usuario)
                .plan(ultima.getPlan())
                .fechaInicio(hoy)
                .fechaFin(hoy.plusDays(DIAS_DURACION_SUSCRIPCION))
                .estado(EstadoSuscripcion.ACTIVA)
                .build();

        return suscripcionRepository.save(nueva);
    }


    //marcamos como caducada toda usscripcion activa cuya fechaFin sea anterior a hoy
    public int marcarCaducadasLasVencidas() {
        LocalDate hoy = LocalDate.now();
        List<Suscripcion> activas = suscripcionRepository.findByEstado(EstadoSuscripcion.ACTIVA);
        int caducadas = 0;
        for (Suscripcion s : activas) {
            if (s.getFechaFin().isBefore(hoy)) {
                s.setEstado(EstadoSuscripcion.CADUCADA);
                caducadas++;
            }
        }
        return caducadas;
    }

    public Suscripcion actualizar(Long id, Suscripcion datosActualizados) {
        Suscripcion suscripcion = obtenerPorId(id);
        suscripcion.setFechaInicio(datosActualizados.getFechaInicio());
        suscripcion.setFechaFin(datosActualizados.getFechaFin());
        suscripcion.setEstado(datosActualizados.getEstado());
        return suscripcionRepository.save(suscripcion);
    }

    public Suscripcion actualizarParcial(Long id, Suscripcion datosParciales) {
        Suscripcion suscripcion = obtenerPorId(id);
        if (datosParciales.getFechaInicio() != null) {
            suscripcion.setFechaInicio(datosParciales.getFechaInicio());
        }
        if (datosParciales.getFechaFin() != null) {
            suscripcion.setFechaFin(datosParciales.getFechaFin());
        }
        if (datosParciales.getEstado() != null) {
            suscripcion.setEstado(datosParciales.getEstado());
        }
        return suscripcionRepository.save(suscripcion);
    }

    public void eliminar(Long id) {
        Suscripcion suscripcion = obtenerPorId(id);
        suscripcionRepository.delete(suscripcion);
    }

    @Transactional(readOnly = true)
    public List<Suscripcion> buscarPorUsuario(Long usuarioId) {
        return suscripcionRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Suscripcion> buscarPorEstado(EstadoSuscripcion estado) {
        return suscripcionRepository.findByEstado(estado);
    }
}
