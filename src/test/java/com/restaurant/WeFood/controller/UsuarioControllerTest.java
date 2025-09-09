package com.restaurant.WeFood.controller;

import com.restaurant.WeFood.repository.UsuarioRepository;
import com.restaurant.WeFood.service.ServiceFood;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = UsuarioController.class,
        excludeAutoConfiguration = { SecurityAutoConfiguration.class }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class) // importa nosso @RestControllerAdvice
class UsuarioControllerTest {

    @Autowired MockMvc mvc;

    // o controller injeta ambos ↓, então precisamos mockar
    @MockitoBean
    ServiceFood serviceFood;
    @MockitoBean
    UsuarioRepository usuarioRepository;

    @Test
    void login_ok_retorna200() throws Exception {
        when(serviceFood.validarLogin(anyString(), anyString()))
                .thenReturn("Login efetuado com sucesso!");

        mvc.perform(post("/usuario/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"email":"gio@example.com","password":"123"}
                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Login efetuado com sucesso!"));
    }

    @Test
    void login_invalido_retorna401() throws Exception {
        when(serviceFood.validarLogin(anyString(), anyString()))
                .thenThrow(new RuntimeException("Email ou Senha incorretos!"));

        mvc.perform(post("/usuario/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"email":"gio@example.com","password":"errada"}
                """))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Email ou Senha incorretos!"));
    }
}
