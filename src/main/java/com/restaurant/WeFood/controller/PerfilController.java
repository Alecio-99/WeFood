package com.restaurant.WeFood.controller;

import com.restaurant.WeFood.entity.Perfil;
import com.restaurant.WeFood.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/perfis")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @PostMapping
    public ResponseEntity<Perfil> criar(@RequestBody Perfil perfil) {
        var saved = perfilService.criar(perfil);
        return ResponseEntity
                .created(URI.create("/api/v1/perfis/" + saved.getId()))
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Perfil>> listar() {
        return ResponseEntity.ok(perfilService.listar());
    }
}
