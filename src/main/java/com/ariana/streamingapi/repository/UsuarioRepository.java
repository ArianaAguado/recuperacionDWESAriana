package com.ariana.streamingapi.repository;

import com.ariana.streamingapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByActivoTrue();
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByEmail(String email);
}
