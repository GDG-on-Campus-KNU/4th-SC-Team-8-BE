package com.example.be.score.service;

import com.example.be.auth.entity.User;
import com.example.be.score.dto.ScoreResponse;
import com.example.be.score.entity.Score;
import com.example.be.score.repository.ScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ScoreService {
    private final ScoreRepository scoreRepository;

    public List<ScoreResponse> getScores(User user){
        List<Score> scoreList = scoreRepository.findAllByUserId(user.getId());

        return scoreList.stream().map(Score::mapToResponse).toList();
    }
}
