package com.restaurant.WeFood.service.validadores;

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

    public String validar(String email, String password){

        if(email == null || email.isBlank()){
            throw new ResourceNotFoundException("É necessário informar o email!");
        }

        Optional<Usuario> existeUser = usuarioRepository.findByEmail(email);
        if (existeUser.isEmpty()){
            throw new ResourceNotFoundException("Usuário não possui cadastro!");
        }

        Usuario usuario = existeUser.get();

        if (password == null || password.isBlank()){
            throw new ResourceNotFoundException("É necessário infromar a senha do usuário.");
        }

        if (!usuario.getPassword().equals(password)){
            throw new ResourceNotFoundException("Email ou senha incorretos!");
        }
        return "Login efetuado com sucesso!";
    }

    @Override
    public void validaSenha(String password) {
        if (password == null || password.isBlank()){
            throw new ResourceNotFoundException("É necessário informar a senha.");
        }
    }
}
