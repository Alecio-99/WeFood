package com.restaurant.WeFood.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfil", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Perfil {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;
}
