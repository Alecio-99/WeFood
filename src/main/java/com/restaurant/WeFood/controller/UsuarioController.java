package com.restaurant.WeFood.controller;

import com.restaurant.WeFood.DTO.AtualizaUsuarioDTO;
import com.restaurant.WeFood.DTO.DetalheUsuarioDTO;
import com.restaurant.WeFood.DTO.UsuarioDTO;
import com.restaurant.WeFood.DTO.ValidaLoginDTO;
import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.exceptions.ResourceNotFoundException;
import com.restaurant.WeFood.repository.UsuarioRepository;
import com.restaurant.WeFood.service.ServiceFood;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("usuario")
@Tag(name = "WeFood", description = "Controller para o crud de cadastro para o usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ServiceFood serviceFood;

    @PostMapping
    public ResponseEntity cadastroUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, UriComponentsBuilder uriBuilder) {
        var usuario = new Usuario(usuarioDTO);

        Optional<Usuario> existsUser = usuarioRepository.findByEmail(usuarioDTO.email());
        if (existsUser.isPresent()) {
            throw new RuntimeException("O email utilizado já possui cadastro!");
        }
        usuarioRepository.save(usuario);

        var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalheUsuarioDTO(usuario));
    }

    @GetMapping({"/", "/{name}"})
    public ResponseEntity<?> buscarPorNome(@PathVariable(required = false) String name) {
        if(name == null || name.isBlank()){
            throw new ResourceNotFoundException("Nome não informado");
        }

        List<Usuario> usuarios = usuarioRepository.findByNameContainingIgnoreCase(name);

        if (usuarios.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum usuário encontrado com o nome:" + name);
        }

        return ResponseEntity.ok(usuarios.stream().map(DetalheUsuarioDTO::new).toList());
    }

    @PostMapping("login")
    public String login(@RequestBody ValidaLoginDTO validaLoginDTO) {
        return serviceFood.validarLogin(validaLoginDTO.email(), validaLoginDTO.password());
    }

    @PutMapping("/senha/{id}")
    public ResponseEntity atualizarSenha(@PathVariable Long id, @RequestBody @Valid ValidaLoginDTO validaLoginDTO) {
        var usuario = usuarioRepository.getReferenceById(id);

        serviceFood.validaSenha(validaLoginDTO.password());

        usuario.atualizarPassWord(validaLoginDTO.password());

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new DetalheUsuarioDTO(usuario));
    }

    @PatchMapping("/{id}")
    public ResponseEntity atualizaUser(@PathVariable Long id, @RequestBody @Valid AtualizaUsuarioDTO atualizaUsuarioDTO) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o id: " + id));
       // usuario.atualizarUsuario(atualizaUsuarioDTO);

        serviceFood.validaAtualizaUsuario(atualizaUsuarioDTO);

        usuario.atualizarUsuario(atualizaUsuarioDTO);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new DetalheUsuarioDTO(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletaUsuario(@PathVariable Long id){
        if(!usuarioRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
          usuarioRepository.deleteById(id);
       return ResponseEntity.noContent().build();
    }
}