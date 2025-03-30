package com.example.be.game.repository;

import com.example.be.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByYoutubeLink(String youtubeLink);
    @Query(value = "SELECT * FROM game ORDER BY RAND() LIMIT 20", nativeQuery = true)
    List<Game> findRandomGames();
}
