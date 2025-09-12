package com.restaurant.WeFood.entity;


import com.restaurant.WeFood.DTO.DadosEnderecoDTO;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@AllArgsConstructor
@Data
public class Endereco {

    private String rua;
    private String numero;
    private String cidade;
    private String cep;

    public Endereco (){

    }

    public Endereco(DadosEnderecoDTO dadosEnderecoDTO){
        this.rua = dadosEnderecoDTO.rua();
        this.numero = dadosEnderecoDTO.numero();
        this.cidade = dadosEnderecoDTO.cidade();
        this.cep = dadosEnderecoDTO.cep();

    }

    public void atualizarEndereco(DadosEnderecoDTO dadosEnderecoDTO){
          if(dadosEnderecoDTO.rua() != null){
              this.rua = dadosEnderecoDTO.rua();
          }
        if(dadosEnderecoDTO.numero() != null){
            this.numero = dadosEnderecoDTO.numero();
        }
        if(dadosEnderecoDTO.cidade() != null){
            this.cidade = dadosEnderecoDTO.cidade();
        }
        if(dadosEnderecoDTO.cep() != null){
            this.cep = dadosEnderecoDTO.cep();
        }
    }

}
