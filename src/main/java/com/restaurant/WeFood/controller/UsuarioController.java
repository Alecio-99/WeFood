package com.restaurant.WeFood.controller;

import com.restaurant.WeFood.DTO.DetalheUsuarioDTO;
import com.restaurant.WeFood.DTO.UsuarioDTO;
import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
  public  ResponseEntity cadastroUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, UriComponentsBuilder uriBuilder){
      var usuario = new Usuario(usuarioDTO);
      usuarioRepository.save(usuario);

     var uri =uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
     return ResponseEntity.created(uri).body(new DetalheUsuarioDTO(usuario));
    }
    @GetMapping("/nome/{name}")
    public ResponseEntity<?> buscarPorNome(@PathVariable String name) {
        List<Usuario> usuarios = usuarioRepository.findByNameContainingIgnoreCase(name);

        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuarios.stream().map(DetalheUsuarioDTO::new).toList());
    }
}
