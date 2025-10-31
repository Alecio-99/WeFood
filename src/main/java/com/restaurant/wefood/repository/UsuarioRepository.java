package com.restaurant.wefood.repository;

import com.restaurant.wefood.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByNameContainingIgnoreCase(String name);
    Optional<Usuario> findByEmailIgnoreCase(String email); //
    @Query("select u from Usuario u join u.perfil p where lower(p.nome) = lower(:nome)")
    List<Usuario> findByPerfilNome(String nome);
}
