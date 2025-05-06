package com.example.be.game.repository;

import com.example.be.game.dto.GameResponse;
import com.example.be.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByYoutubeLink(String youtubeLink);
    @Query(value = "SELECT youtube_link AS youtubeLink FROM game ORDER BY RAND() LIMIT 20", nativeQuery = true)
    List<GameResponse> findRandomGames();
}
