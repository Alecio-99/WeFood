package com.restaurant.WeFood.controller;

import com.restaurant.WeFood.DTO.DetalheUsuarioDTO;
import com.restaurant.WeFood.DTO.UsuarioDTO;
import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    ResponseEntity cadastroUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, UriComponentsBuilder uriBuilder){
      var usuario = new Usuario(usuarioDTO);
      usuarioRepository.save(usuario);

     var uri =uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
     return ResponseEntity.created(uri).body(new DetalheUsuarioDTO(usuario));
    }
}
