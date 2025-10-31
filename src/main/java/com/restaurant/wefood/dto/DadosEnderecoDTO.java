package com.restaurant.wefood.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEnderecoDTO (
         @NotBlank(message = "O nome da rua não pode ser nulo ou vazio")
         String rua,
         @NotBlank(message = "O numero não pode ser nulo ou vazio")
         String numero,
         @NotBlank(message = "O nome da cidade não pode ser nulo ou vazio")
         String cidade,
         @NotBlank(message = "O cep não pode ser nulo ou vazio")
         @Pattern(regexp = ("\\d{8}"))
         String cep
){
}
