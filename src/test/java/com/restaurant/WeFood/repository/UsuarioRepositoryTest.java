package com.restaurant.WeFood.repository;

import com.restaurant.WeFood.DTO.DadosEnderecoDTO;
import com.restaurant.WeFood.DTO.UsuarioDTO;
import com.restaurant.WeFood.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    private Usuario mkUser(String nome, String email, String senha) {
        var enderecoDTO = new DadosEnderecoDTO(
                "Rua Teste", "123", "Sorocaba", "18000000"
        );
        var dto = new UsuarioDTO(nome, email, senha, enderecoDTO);
        return new Usuario(dto);
    }

    @BeforeEach
    void seed() {
        repository.deleteAll();
        repository.saveAll(List.of(
                mkUser("Giovana Scalabrini", "gio@example.com", "123"),
                mkUser("Gustavo Silva",      "gus@example.com", "abc"),
                mkUser("Ana GIO",            "ana@example.com", "zzz")
        ));
    }

    @Test
    void findByEmail_encontraUsuarioCorreto() {
        Optional<Usuario> opt = repository.findByEmail("gio@example.com");

        assertThat(opt).isPresent();
        assertThat(opt.get().getName()).isEqualTo("Giovana Scalabrini");
    }

    @Test
    void findByEmail_retornaVazio_quandoNaoExiste() {
        Optional<Usuario> opt = repository.findByEmail("naoexiste@example.com");
        assertThat(opt).isEmpty();
    }

    @Test
    void findByNameContainingIgnoreCase_buscaParcialSemDiferenciarCaixa() {
        List<Usuario> resultados = repository.findByNameContainingIgnoreCase("gio");

        assertThat(resultados)
                .extracting(Usuario::getEmail)
                .containsExactlyInAnyOrder("gio@example.com", "ana@example.com");
    }
}
