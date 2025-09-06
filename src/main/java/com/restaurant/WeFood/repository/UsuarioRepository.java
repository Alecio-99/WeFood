package com.restaurant.WeFood.repository;

import com.restaurant.WeFood.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
