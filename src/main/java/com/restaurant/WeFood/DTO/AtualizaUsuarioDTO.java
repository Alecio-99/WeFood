package com.restaurant.WeFood.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizaUsuarioDTO(

        String name,
        @Email
        String email,
        DadosEnderecoDTO endereco
) {
}
