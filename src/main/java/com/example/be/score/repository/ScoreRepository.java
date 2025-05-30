package com.example.be.score.repository;

import com.example.be.score.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findAllByUserId(long userId);
}
