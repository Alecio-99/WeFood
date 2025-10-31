package com.restaurant.wefood.dto;

import jakarta.validation.constraints.Email;

public record AtualizaUsuarioDTO(
        String name,
        @Email String email,
        DadosEnderecoDTO endereco,
        Long perfilId // opcional
) {}
