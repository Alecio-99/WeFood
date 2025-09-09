package com.restaurant.WeFood.service;

import com.restaurant.WeFood.service.validadores.ValidaLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFood {


    @Autowired
    ValidaLogin valida;

      public String validarLogin(String email, String password){
          return valida.validar(email, password);
    }
}
