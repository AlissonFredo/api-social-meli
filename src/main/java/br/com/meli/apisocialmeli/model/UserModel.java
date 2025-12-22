package br.com.meli.apisocialmeli.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserTipo tipo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    private Set<FollowModel> seguindo = new HashSet<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private Set<FollowModel> seguidores = new HashSet<>();

    public UserTipo getTipo() {
        return tipo;
    }

    public void setTipo(UserTipo tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<FollowModel> getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(Set<FollowModel> seguindo) {
        this.seguindo = seguindo;
    }

    public Set<FollowModel> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(Set<FollowModel> seguidores) {
        this.seguidores = seguidores;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
