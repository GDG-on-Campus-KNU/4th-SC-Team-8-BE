package com.example.be.game.entity;

import com.example.be.game.dto.GameResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String youtubeLink;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String landmark;

    public Game(String youtubeLink, String landmark){
        this.youtubeLink = youtubeLink;
        this.landmark = landmark;
    }

    public GameResponse mapToResponse(){
        return new GameResponse(this.youtubeLink);
    }
}
