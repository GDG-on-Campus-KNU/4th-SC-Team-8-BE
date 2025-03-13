package com.example.be.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    public User(String email, String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public void update(String password){
        this.password = password;
    }
}
