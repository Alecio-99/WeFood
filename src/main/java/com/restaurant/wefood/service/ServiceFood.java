package com.restaurant.wefood.service;

import com.restaurant.wefood.dto.AtualizaUsuarioDTO;
import com.restaurant.wefood.service.validadores.ValidaLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFood {


    @Autowired
    ValidaLogin valida;

      public String validarLogin(String email, String password){
          return valida.validar(email, password);
    }
    public void validaSenha(String password){
          valida.validaSenha(password);
    }
    public void validaAtualizaUsuario(AtualizaUsuarioDTO atualizaUsuarioDTO){
           valida.validaAtualizaUsuario(atualizaUsuarioDTO);
    }
}
