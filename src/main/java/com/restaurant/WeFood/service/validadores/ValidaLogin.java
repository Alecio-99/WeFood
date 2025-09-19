package com.restaurant.WeFood.service.validadores;

import com.restaurant.WeFood.DTO.AtualizaUsuarioDTO;

public interface ValidaLogin {

    String validar(String email, String password);

    void validaSenha(String password);

    void validaAtualizaUsuario(AtualizaUsuarioDTO atualizaUsuarioDTO);
}
