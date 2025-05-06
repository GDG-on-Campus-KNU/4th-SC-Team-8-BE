package com.example.be.score.entity;

import com.example.be.auth.entity.User;
import com.example.be.game.entity.Game;
import com.example.be.score.dto.ScoreResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Game game;

    @NotNull
    private long score;

    public Score(User user, Game game, long score){
        this.user = user;
        this.game = game;
        this.score = score;
    }

    public ScoreResponse mapToResponse(){
        return new ScoreResponse(game.getYoutubeLink(), score);
    }
}
