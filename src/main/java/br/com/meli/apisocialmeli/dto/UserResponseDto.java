package br.com.meli.apisocialmeli.dto;

import br.com.meli.apisocialmeli.model.UserTipo;

public class UserResponseDto {
    private Long id;
    private String nome;
    private UserTipo tipo;

    public UserResponseDto(Long id, String nome, UserTipo tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UserTipo getTipo() {
        return tipo;
    }

    public void setTipo(UserTipo tipo) {
        this.tipo = tipo;
    }
}
