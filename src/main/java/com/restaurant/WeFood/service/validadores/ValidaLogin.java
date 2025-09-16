package com.restaurant.WeFood.service.validadores;

public interface ValidaLogin {

    String validar(String email, String password);

    void validaSenha(String password);
}
