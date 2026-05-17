package com.ariana.streamingapi.service;

import com.ariana.streamingapi.exception.RecursoDuplicadoException;
import com.ariana.streamingapi.exception.RecursoNoEncontradoException;
import com.ariana.streamingapi.model.Plan;
import com.ariana.streamingapi.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service @Transactional
public class PlanService {
    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository){
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public List<Plan> listarTodos(){
        return planRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Plan obtenerPorId(Long id){
        return planRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException("Plan no encontrado con id: "+id));
    }

    public Plan crear(Plan plan){
        if(planRepository.existsByNombre(plan.getNombre())){
            throw new RecursoDuplicadoException("Plan existente con el nombre: "+plan.getNombre());
        }
        return planRepository.save(plan);
    }

    public Plan actualizar(Long id, Plan datosActualizados){
        Plan plan=obtenerPorId(id);
        plan.setNombre(datosActualizados.getNombre());
        plan.setPrecioMensual(datosActualizados.getPrecioMensual());
        plan.setCalidadMaxima(datosActualizados.getCalidadMaxima());
        plan.setPantallasSimultaneas(datosActualizados.getPantallasSimultaneas());
        plan.setActivo(datosActualizados.isActivo());
        return planRepository.save(plan);
    }

    public Plan actualizarParcial(Long id, Plan datosParciales){
        Plan plan=obtenerPorId(id);
        if(datosParciales.getNombre()!=null){
            plan.setNombre(datosParciales.getNombre());
        }
        if (datosParciales.getPrecioMensual() != null) {
            plan.setPrecioMensual(datosParciales.getPrecioMensual());
        }
        if (datosParciales.getCalidadMaxima() != null) {
            plan.setCalidadMaxima(datosParciales.getCalidadMaxima());
        }
        if (datosParciales.getPantallasSimultaneas() != null) {
            plan.setPantallasSimultaneas(datosParciales.getPantallasSimultaneas());
        }
        return planRepository.save(plan);
    }

    public void eliminar(Long id){
        Plan plan=obtenerPorId(id);
        planRepository.delete(plan);
    }

    @Transactional(readOnly = true)
    public List<Plan> listarActivos(){
        return planRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Plan> buscarPorRangoPrecio(BigDecimal min, BigDecimal max){
        return planRepository.findByPrecioMensualBetween(min,max);
    }
}
//transactional hace que todo movimiento sea una transacción y si da error, hace rollback y vuelve para atrás
//readonly true es para optimizar lops métodos de solo lectura