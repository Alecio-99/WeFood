package com.restaurant.WeFood.service.validadores;

import com.restaurant.WeFood.DTO.AtualizaUsuarioDTO;
import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.exceptions.ResourceNotFoundException;
import com.restaurant.WeFood.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidaLoginImpl implements ValidaLogin {

    @Autowired
    UsuarioRepository usuarioRepository;

    public String validar(String email, String password) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("É necessário informar o email!");
        }

        Optional<Usuario> existeUser = usuarioRepository.findByEmail(email);
        if (existeUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não possui cadastro!");
        }

        Usuario usuario = existeUser.get();

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("É necessário infromar a senha do usuário.");
        }

        if (!usuario.getPassword().equals(password)) {
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
            var endereco = atualizaUsuarioDTO.endereco();

            if (endereco.rua() != null && endereco.rua().isBlank()) {
                throw new IllegalArgumentException("Rua não pode ser vazia");
            }
            if (endereco.cep() != null && endereco.cep().isBlank()) {
                throw new IllegalArgumentException("Cep não pode ser vazio");
            }
            if (endereco.cidade() != null && endereco.cidade().isBlank()) {
                throw new IllegalArgumentException("O campo cidade não pode ser vazio");
            }
            if (endereco.numero() != null && endereco.numero().isBlank()) {
                throw new IllegalArgumentException("Número não pode ser vazio");
            }
        }
    }
}