package com.ariana.streamingapi.service;

import com.ariana.streamingapi.exception.RecursoDuplicadoException;
import com.ariana.streamingapi.exception.RecursoNoEncontradoException;
import com.ariana.streamingapi.model.Usuario;
import com.ariana.streamingapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service @Transactional
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id " + id));
    }

    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RecursoDuplicadoException(
                    "Ya existe un usuario con el email " + usuario.getEmail());
        }
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datosActualizados) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEmail(datosActualizados.getEmail());
        usuario.setNombre(datosActualizados.getNombre());
        usuario.setActivo(datosActualizados.isActivo());
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarParcial(Long id, Usuario datosParciales) {
        Usuario usuario = obtenerPorId(id);
        if (datosParciales.getEmail() != null) {
            usuario.setEmail(datosParciales.getEmail());
        }
        if (datosParciales.getNombre() != null) {
            usuario.setNombre(datosParciales.getNombre());
        }
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con email " + email));
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarActivos() {
        return usuarioRepository.findByActivoTrue();
    }
}

