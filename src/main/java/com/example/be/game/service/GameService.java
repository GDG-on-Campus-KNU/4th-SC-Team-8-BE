package com.example.be.game.service;

import com.example.be.auth.entity.User;
import com.example.be.common.exception.BadRequestException;
import com.example.be.common.exception.ConflictException;
import com.example.be.common.exception.ErrorCode;
import com.example.be.game.dto.AiRequest;
import com.example.be.game.dto.AiResponse;
import com.example.be.game.dto.GameRequest;
import com.example.be.game.dto.GameResponse;
import com.example.be.game.entity.Game;
import com.example.be.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameService {
    private final GameRepository gameRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RestTemplate restTemplate;
    private final long ONE_DAY = 1000L * 60 * 60 * 24;

    public void chkYoutubeLink(GameRequest gameRequest){
        if(gameRepository.existsByYoutubeLink(gameRequest.youtubeLink()))
            throw new ConflictException(ErrorCode.GAME_DUPLICATE_YOUTUBE_LINK);
    }

    @Transactional
    public void postGame(User user, GameRequest gameRequest){
        if(redisTemplate.hasKey(gameRequest.youtubeLink())) throw new ConflictException(ErrorCode.LANDMARK_RENDERING);
        if(gameRepository.existsByYoutubeLink(gameRequest.youtubeLink())) throw new ConflictException(ErrorCode.ALREADY_EXIST_URL);
        try {
            URI uri = UriComponentsBuilder.fromUriString("https://signory.site")
                    .path("/process_youtube")
                    .encode()
                    .build()
                    .toUri();

            AiRequest aiRequest = new AiRequest(gameRequest.youtubeLink());

            ResponseEntity<AiResponse> response = restTemplate.postForEntity(uri, aiRequest, AiResponse.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new BadRequestException(ErrorCode.FAIL_MAKE_LANDMARK);
            }

            String youtubeLink =response.getBody().video_url();

            if(!redisTemplate.hasKey(youtubeLink)){
                redisTemplate.opsForHash().put(youtubeLink, "user", user.getEmail());
                redisTemplate.expire(user.getEmail(), ONE_DAY, TimeUnit.MILLISECONDS);
            }
        } catch(Exception e){
            throw new BadRequestException(ErrorCode.FAIL_MAKE_LANDMARK);
        }
    }

    public List<GameResponse> getGame(){
        List<Game> gameList = gameRepository.findAll();

        return gameList.stream().map(Game::mapToResponse).toList();
    }

    public List<GameResponse> getRandom20Game(){
        return gameRepository.findRandomGames();
    }
}
