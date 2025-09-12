package com.restaurant.WeFood.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
        @NotBlank(message = "O nome não pode ser nulo ou vazio")
        String name,
        @NotBlank(message = "O email não pode ser nulo ou vazio")
        @Email
        String email,
        @NotBlank(message = "A senha não pode ser nulo ou vazio")
        String password,
        @NotNull
        @Valid
        DadosEnderecoDTO endereco
) {


}
