package com.restaurant.WeFood.service.validadores;

import com.restaurant.WeFood.DTO.AtualizaUsuarioDTO;
import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.exceptions.ResourceNotFoundException;
import com.restaurant.WeFood.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 👈 importar
import org.springframework.stereotype.Component;

@Component
public class ValidaLoginImpl implements ValidaLogin {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 👈 injeta o bean definido em SecurityBeans

    @Override
    public String validar(String email, String password) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("É necessário informar o email!");
        }

        // Use ignore case para email (opcional, mas recomendado)
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não possui cadastro!"));

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("É necessário informar a senha do usuário."); // corrige 'infromar'
        }

        // 👇 comparação correta com BCrypt
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new IllegalArgumentException("Email ou senha incorretos!");
        }

        return "Login efetuado com sucesso!";
    }

    @Override
    public void validaSenha(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("É necessário informar a senha.");
        }
    }

    @Override
    public void validaAtualizaUsuario(AtualizaUsuarioDTO atualizaUsuarioDTO) {
        if (atualizaUsuarioDTO.name() != null && atualizaUsuarioDTO.name().isBlank()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio");
        }
        if (atualizaUsuarioDTO.email() != null && atualizaUsuarioDTO.email().isBlank()) {
            throw new IllegalArgumentException("O email não pode ser nulo ou vazio");
        }
        if (atualizaUsuarioDTO.endereco() != null) {
            var e = atualizaUsuarioDTO.endereco();
            if (e.rua() != null && e.rua().isBlank()) throw new IllegalArgumentException("Rua não pode ser vazia");
            if (e.cep() != null && e.cep().isBlank()) throw new IllegalArgumentException("Cep não pode ser vazio");
            if (e.cidade() != null && e.cidade().isBlank()) throw new IllegalArgumentException("O campo cidade não pode ser vazio");
            if (e.numero() != null && e.numero().isBlank()) throw new IllegalArgumentException("Número não pode ser vazio");
        }
    }
}
