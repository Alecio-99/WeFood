package com.restaurant.WeFood.repository;

import com.restaurant.WeFood.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNomeIgnoreCase(String nome);
}
