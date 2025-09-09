package com.restaurant.WeFood.service;

import com.restaurant.WeFood.service.validadores.ValidaLogin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceFoodTest {

    @Mock
    ValidaLogin valida;

    @InjectMocks
    ServiceFood service;

    @Test
    void validarLogin_retornaMensagemDeSucesso() {
        when(valida.validar("gio@example.com", "123"))
                .thenReturn("Login efetuado com sucesso!");

        String r = service.validarLogin("gio@example.com", "123");

        assertThat(r).isEqualTo("Login efetuado com sucesso!");
    }
}
