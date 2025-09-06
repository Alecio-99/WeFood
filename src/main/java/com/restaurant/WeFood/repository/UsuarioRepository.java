package com.restaurant.WeFood.repository;

import com.restaurant.WeFood.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByNameContainingIgnoreCase(String name);

}
