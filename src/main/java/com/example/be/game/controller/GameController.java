package com.example.be.game.controller;

import com.example.be.common.annotation.ApiErrorCodeExamples;
import com.example.be.common.exception.ErrorCode;
import com.example.be.game.dto.GameRequest;
import com.example.be.game.dto.GameResponse;
import com.example.be.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/game")
@Tag(name = "게임 등록 기능", description = "유튜브 링크 등록 및 조회를 위한 api")
public class GameController {
    private final GameService gameService;

    @Operation(summary = "유튜브 링크 존재 여부 검사",
            description = "유튜브 주소를 입력값으로 받아 DB에 존재하는지 확인한다.")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED, ErrorCode.GAME_DUPLICATE_YOUTUBE_LINK})
    @PostMapping("/check-link")
    public ResponseEntity<?> register(@RequestBody GameRequest gameRequest){
        gameService.chkYoutubeLink(gameRequest);

        return ResponseEntity.ok(ErrorCode.OK);
    }

    @Operation(summary = "유튜브 링크 등록",
            description = "유튜브 주소를 입력값으로 받아 ai서버에서 랜드마크를 추출하고 DB에 등록한다.")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED})
    @PostMapping()
    public ResponseEntity<?> postGame(@RequestBody GameRequest gameRequest){
        gameService.postGame(gameRequest);

        return ResponseEntity.ok(ErrorCode.OK);
    }

    @Operation(summary = "게임 목록 조회",
            description = "현재 데이터베이스에 등록된 게임 목록을 조회한다.(페이지네이션 필요하시면 말해주세요!)")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED})
    @GetMapping()
    public ResponseEntity<List<GameResponse>> getGame(){
        return ResponseEntity.ok(gameService.getGame());
    }
}
