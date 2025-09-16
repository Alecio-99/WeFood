package com.restaurant.WeFood.entity;

import com.restaurant.WeFood.DTO.AtualizaUsuarioDTO;
import com.restaurant.WeFood.DTO.UsuarioDTO;
import com.restaurant.WeFood.DTO.ValidaLoginDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "E-mail")
    private String email;
    @Column(name = "Nome")
    private String name;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;

    @Embedded
    private Endereco endereco;

    public Usuario(@Valid UsuarioDTO usuarioDTO) {
        this.email = usuarioDTO.email();
        this.name = usuarioDTO.name();
        this.password = usuarioDTO.password();
        this.endereco = new Endereco(usuarioDTO.endereco());
    }

    public void atualizarPassWord(String password){
        if(password != null) {
            this.password = password;
        }

    }
    public void atualizarUsuario(@Valid AtualizaUsuarioDTO atualizaUsuarioDTO){
        if (atualizaUsuarioDTO.name() != null){
            this.name = atualizaUsuarioDTO.name();
        }
        if (atualizaUsuarioDTO.email() != null){
            this.email = atualizaUsuarioDTO.email();
        }
        if (atualizaUsuarioDTO.endereco() != null){
            this.endereco.atualizarEndereco(atualizaUsuarioDTO.endereco());
        }
    }
}
