package com.restaurant.WeFood.service.validadores;

import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidaLoginImpl implements ValidaLogin {

    @Autowired
    UsuarioRepository usuarioRepository;

    public String validar(String email, String password){
        Optional<Usuario> existsUser = usuarioRepository.findByEmail(email);
        if(!existsUser.isPresent()){
            throw new RuntimeException("Usuário não possui cadastro");
        }

            Usuario usuario = existsUser.get();

                 if (!usuario.getPassword().equals(password)) {
                     throw new RuntimeException("Email ou Senha incorretos!");
                 }

                 return "Login efetuado com sucesso!";
    }
}
