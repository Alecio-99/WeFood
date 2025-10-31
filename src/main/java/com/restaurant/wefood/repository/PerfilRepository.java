package com.restaurant.wefood.repository;

import com.restaurant.wefood.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNomeIgnoreCase(String nome);
}
