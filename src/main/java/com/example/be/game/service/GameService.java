package com.example.be.game.service;

import com.example.be.common.exception.ConflictException;
import com.example.be.common.exception.ErrorCode;
import com.example.be.game.dto.GameRequest;
import com.example.be.game.dto.GameResponse;
import com.example.be.game.entity.Game;
import com.example.be.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Transactional(readOnly = true)
    public void chkYoutubeLink(GameRequest gameRequest){
        if(gameRepository.existsByYoutubeLink(gameRequest.youtubeLink()))
            throw new ConflictException(ErrorCode.GAME_DUPLICATE_YOUTUBE_LINK);
    }

    @Transactional
    public void postGame(GameRequest gameRequest){

    }

    @Transactional(readOnly = true)
    public List<GameResponse> getGame(){
        List<Game> gameList = gameRepository.findAll();

        return gameList.stream().map(Game::mapToResponse).toList();
    }
}
