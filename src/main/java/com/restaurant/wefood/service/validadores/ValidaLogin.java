package com.restaurant.wefood.service.validadores;

import com.restaurant.wefood.dto.AtualizaUsuarioDTO;

public interface ValidaLogin {

    String validar(String email, String password);

    void validaSenha(String password);

    void validaAtualizaUsuario(AtualizaUsuarioDTO atualizaUsuarioDTO);
}
