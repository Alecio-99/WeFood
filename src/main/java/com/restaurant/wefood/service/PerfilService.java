package com.restaurant.wefood.service;

import com.restaurant.wefood.entity.Perfil;
import com.restaurant.wefood.exceptions.ResourceNotFoundException;
import com.restaurant.wefood.repository.PerfilRepository;
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

    public void deletar(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new ResourceNotFoundException("Perfil não encontrado com o id: " + id);
        }
        perfilRepository.deleteById(id);
    }
}
