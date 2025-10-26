package com.restaurant.WeFood.DTO;

import com.restaurant.WeFood.entity.Usuario;

public record DetalheUsuarioDTO(
        String name,
        String email,
        String perfil
) {
    public DetalheUsuarioDTO(Usuario u) {
        this(u.getName(), u.getEmail(), u.getPerfil().getNome());
    }
}
