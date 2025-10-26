package com.restaurant.WeFood.service;

import com.restaurant.WeFood.entity.Perfil;
import com.restaurant.WeFood.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository perfilRepository;

    @Transactional
    public Perfil criar(Perfil perfil) {
        if (perfil == null || perfil.getNome() == null || perfil.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do perfil é obrigatório.");
        }

        perfilRepository.findByNomeIgnoreCase(perfil.getNome())
                .ifPresent(p -> { throw new IllegalArgumentException("Perfil já existe: " + p.getNome()); });

        return perfilRepository.save(perfil);
    }

    @Transactional(readOnly = true)
    public List<Perfil> listar() {
        return perfilRepository.findAll();
    }
}
