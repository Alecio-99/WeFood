package com.restaurant.WeFood.entity;

import com.restaurant.WeFood.DTO.AtualizaUsuarioDTO;
import com.restaurant.WeFood.DTO.UsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "nome", nullable = false, length = 150)
    private String name;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    @UpdateTimestamp
    private LocalDateTime ultimaAtualizacao;

    @Embedded
    private Endereco endereco;

    public Usuario(@Valid UsuarioDTO dto) {
        this.email = dto.email();
        this.name = dto.name();
        this.password = dto.password();
        this.endereco = new Endereco(dto.endereco());
    }

    public void atualizarPassWord(String password){
        if(password != null) this.password = password;
    }

    public void atualizarUsuario(@Valid AtualizaUsuarioDTO dto){
        if (dto.name() != null) this.name = dto.name();
        if (dto.email() != null) this.email = dto.email();
        if (dto.endereco() != null) {
            if (this.endereco == null) this.endereco = new Endereco();
            this.endereco.atualizarEndereco(dto.endereco());
        }
    }
}
