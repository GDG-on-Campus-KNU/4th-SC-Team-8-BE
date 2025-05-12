package com.example.be.game.service;

import com.example.be.auth.entity.User;
import com.example.be.common.exception.ErrorCode;
import com.example.be.common.exception.ErrorException;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Comparator;
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
    private final String AI_SERVER_URL = "https://signory.site";
    private final String LANDMARK_PATH = "/process_youtube";

    public void chkYoutubeLink(GameRequest gameRequest){
        if(gameRepository.existsByYoutubeLink(gameRequest.youtubeLink()))
            throw new ErrorException(ErrorCode.GAME_DUPLICATE_YOUTUBE_LINK);
    }

    @Transactional
    public void postGame(User user, GameRequest gameRequest){
        if(redisTemplate.hasKey(gameRequest.youtubeLink())) throw new ErrorException(ErrorCode.LANDMARK_RENDERING);
        if(gameRepository.existsByYoutubeLink(gameRequest.youtubeLink())) throw new ErrorException(ErrorCode.ALREADY_EXIST_URL);
        try {
            URI uri = UriComponentsBuilder.fromUriString(AI_SERVER_URL)
                    .path(LANDMARK_PATH)
                    .encode()
                    .build()
                    .toUri();

            AiRequest aiRequest = new AiRequest(gameRequest.youtubeLink());

            ResponseEntity<AiResponse> response = restTemplate.postForEntity(uri, aiRequest, AiResponse.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new ErrorException(ErrorCode.FAIL_MAKE_LANDMARK);
            }

            String youtubeLink =response.getBody().video_url();

            if(!redisTemplate.hasKey(youtubeLink)){
                redisTemplate.opsForHash().put(youtubeLink, "user", user.getEmail());
                redisTemplate.expire(user.getEmail(), ONE_DAY, TimeUnit.MILLISECONDS);
            }
        } catch(Exception e){
            throw new ErrorException(ErrorCode.FAIL_MAKE_LANDMARK);
        }
    }

    public List<GameResponse> getGame(){
        List<Game> gameList = gameRepository.findAll();

        return gameList.stream().map(Game::mapToResponse).toList();
    }

    public List<GameResponse> getRandom20Game(){
        return gameRepository.findRandomGames();
    }

    public String hashGameResponseList() {
        try {
            List<GameResponse> list = gameRepository.findGames();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            list.stream()
                    .sorted(Comparator.comparing(GameResponse::youtubeLink))
                    .forEach(r -> digest.update(r.youtubeLink().trim().toLowerCase().getBytes(StandardCharsets.UTF_8)));
            byte[] hash = digest.digest();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
