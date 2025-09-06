package com.restaurant.WeFood.DTO;

import com.restaurant.WeFood.entity.Usuario;

public record DetalheUsuarioDTO(

        String name,
        String email
) {
    public DetalheUsuarioDTO(Usuario usuario){
        this(usuario.getName(), usuario.getEmail());
    }
}
