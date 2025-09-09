package com.restaurant.WeFood.service.validadores;

import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidaLoginImplTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    ValidaLoginImpl valida;

    private static Usuario usuario(String email, String senha) {
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setPassword(senha);
        return u;
    }

    @Test
    void validar_retornaMensagemDeSucesso_quandoSenhaCorreta() {
        when(usuarioRepository.findByEmail("gio@example.com"))
                .thenReturn(Optional.of(usuario("gio@example.com", "123")));

        String resultado = valida.validar("gio@example.com", "123");

        // mensagem real da sua implementação
        assertThat(resultado).isEqualTo("Login efetuado com sucesso!");
    }

    @Test
    void validar_lancaExcecao_quandoSenhaIncorreta() {
        when(usuarioRepository.findByEmail("gio@example.com"))
                .thenReturn(Optional.of(usuario("gio@example.com", "123")));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> valida.validar("gio@example.com", "errada"));

        assertThat(ex.getMessage()).isEqualTo("Email ou Senha incorretos!");
    }
}
