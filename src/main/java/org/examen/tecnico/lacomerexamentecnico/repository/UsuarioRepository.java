package org.examen.tecnico.lacomerexamentecnico.repository;

import org.examen.tecnico.lacomerexamentecnico.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCorreo(String correo);
    Page<Usuario> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Usuario> findAll(Pageable pageable);
}
