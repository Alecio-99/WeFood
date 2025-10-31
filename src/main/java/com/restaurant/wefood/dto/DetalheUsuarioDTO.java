package com.restaurant.wefood.dto;

import com.restaurant.wefood.entity.Usuario;

public record DetalheUsuarioDTO(
        String name,
        String email,
        String perfil
) {
    public DetalheUsuarioDTO(Usuario u) {
        this(u.getName(), u.getEmail(), u.getPerfil().getNome());
    }
}
