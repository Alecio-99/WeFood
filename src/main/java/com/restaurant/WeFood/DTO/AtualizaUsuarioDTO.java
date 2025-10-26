package com.restaurant.WeFood.DTO;

import jakarta.validation.constraints.Email;

public record AtualizaUsuarioDTO(
        String name,
        @Email String email,
        DadosEnderecoDTO endereco,
        Long perfilId // opcional
) {}
