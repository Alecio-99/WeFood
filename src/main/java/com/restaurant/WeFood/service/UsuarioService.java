package com.restaurant.WeFood.service;

import com.restaurant.WeFood.DTO.AtualizaUsuarioDTO;
import com.restaurant.WeFood.DTO.DetalheUsuarioDTO;
import com.restaurant.WeFood.DTO.UsuarioDTO;
import com.restaurant.WeFood.DTO.ValidaLoginDTO;
import com.restaurant.WeFood.entity.Usuario;
import com.restaurant.WeFood.enums.PerfilUsuario;
import com.restaurant.WeFood.exceptions.ResourceNotFoundException;
import com.restaurant.WeFood.repository.PerfilRepository;
import com.restaurant.WeFood.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario criar(UsuarioDTO dto) {
        usuarioRepository.findByEmailIgnoreCase(dto.email())
                .ifPresent(u -> { throw new IllegalArgumentException("O email utilizado já possui cadastro!"); });

        // valida perfil no banco
        var perfil = perfilRepository.findById(dto.perfilId())
                .orElseThrow(() -> new IllegalArgumentException("Perfil inexistente: id=" + dto.perfilId()));

        var usuario = new Usuario(dto);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setPerfil(perfil);

        return usuarioRepository.save(usuario);
    }

    public List<DetalheUsuarioDTO> buscarPorNome(String name) {
        if (name == null || name.isBlank()) {
            throw new ResourceNotFoundException("Nome não informado");
        }
        var lista = usuarioRepository.findByNameContainingIgnoreCase(name);
        if (lista.isEmpty()) throw new ResourceNotFoundException("Nenhum usuário encontrado com o nome: " + name);
        return lista.stream().map(DetalheUsuarioDTO::new).toList();
    }

    public DetalheUsuarioDTO atualizarSenha(Long id, ValidaLoginDTO validaLoginDTO) {
        var u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o id: " + id));
        if (validaLoginDTO.password() == null || validaLoginDTO.password().isBlank()) {
            throw new IllegalArgumentException("É necessário informar a senha.");
        }
        u.atualizarPassWord(passwordEncoder.encode(validaLoginDTO.password()));
        u.setUltimaAtualizacao(LocalDateTime.now());
        return new DetalheUsuarioDTO(usuarioRepository.save(u));
    }

    public DetalheUsuarioDTO atualizarParcial(Long id, AtualizaUsuarioDTO dto) {
        var u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o id: " + id));

        u.atualizarUsuario(dto);

        if (dto.perfilId() != null) {
            var perfil = perfilRepository.findById(dto.perfilId())
                    .orElseThrow(() -> new IllegalArgumentException("Perfil inexistente: id=" + dto.perfilId()));
            u.setPerfil(perfil);
        }

        u.setUltimaAtualizacao(LocalDateTime.now());
        return new DetalheUsuarioDTO(usuarioRepository.save(u));
    }

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario não encontrado com o id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public List<DetalheUsuarioDTO> listarPorPerfilNome(String nome) {
        var users = usuarioRepository.findByPerfilNome(nome);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum usuário com perfil: " + nome);
        }
        return users.stream().map(DetalheUsuarioDTO::new).toList();
    }
}