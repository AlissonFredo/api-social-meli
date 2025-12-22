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
    private Set<FollowModel> following = new HashSet<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private Set<FollowModel> followers = new HashSet<>();
}
