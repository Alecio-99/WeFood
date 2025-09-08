package com.restaurant.WeFood.service;

import com.restaurant.WeFood.DTO.ValidaLoginDTO;
import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.repository.UsuarioRepository;
import com.restaurant.WeFood.service.validadores.Valida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceFood {


    @Autowired
    Valida valida;

      public String validarLogin(String email, String password){
          return valida.validar(email, password);
    }
}
