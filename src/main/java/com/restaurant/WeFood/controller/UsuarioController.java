package com.restaurant.WeFood.controller;

import com.restaurant.WeFood.DTO.AtualizaUsuarioDTO;
import com.restaurant.WeFood.DTO.DetalheUsuarioDTO;
import com.restaurant.WeFood.DTO.UsuarioDTO;
import com.restaurant.WeFood.DTO.ValidaLoginDTO;
import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.enums.PerfilUsuario;
import com.restaurant.WeFood.service.ServiceFood;
import com.restaurant.WeFood.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/v1/usuario")
@Tag(name = "WeFood", description = "Controller para o crud de cadastro para o usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ServiceFood serviceFood;

    @PostMapping
    public ResponseEntity<?> cadastroUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO,
                                             UriComponentsBuilder uriBuilder) {
        Usuario usuario = usuarioService.criar(usuarioDTO);
        var uri = uriBuilder.path("/api/v1/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalheUsuarioDTO(usuario));
    }

    // Mantendo sua rota original com PathVariable
    @GetMapping({"/", "/{name}"})
    public ResponseEntity<?> buscarPorNome(@PathVariable(required = false) String name) {
        List<DetalheUsuarioDTO> resposta = usuarioService.buscarPorNome(name);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid ValidaLoginDTO validaLoginDTO) {
        var msg = serviceFood.validarLogin(validaLoginDTO.email(), validaLoginDTO.password());
        return ResponseEntity.ok().body(msg);
    }

    @PutMapping("/senha/{id}")
    public ResponseEntity<?> atualizarSenha(@PathVariable Long id,
                                            @RequestBody @Valid ValidaLoginDTO validaLoginDTO) {
        DetalheUsuarioDTO dto = usuarioService.atualizarSenha(id, validaLoginDTO);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizaUser(@PathVariable Long id,
                                          @RequestBody @Valid AtualizaUsuarioDTO atualizaUsuarioDTO) {
        DetalheUsuarioDTO dto = usuarioService.atualizarParcial(id, atualizaUsuarioDTO);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaUsuario(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/perfil/{nome}")
    public ResponseEntity<List<DetalheUsuarioDTO>> listarPorPerfil(@PathVariable String nome) {
        return ResponseEntity.ok(usuarioService.listarPorPerfilNome(nome));
    }

}
