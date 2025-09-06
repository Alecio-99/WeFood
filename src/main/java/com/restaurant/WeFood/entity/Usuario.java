package com.restaurant.WeFood.entity;

import com.restaurant.WeFood.DTO.UsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "E-mail")
    private String email;
    @Column(name = "Nome")
    private String name;
    @Column(name = "PassWord")
    private String password;

    public Usuario(@Valid UsuarioDTO usuarioDTO) {
        this.email = usuarioDTO.email();
        this.name = usuarioDTO.name();
    }
}
