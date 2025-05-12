package com.example.be.score.controller;

import com.example.be.auth.service.CustomUserDetails;
import com.example.be.common.annotation.ApiErrorCodeExamples;
import com.example.be.common.exception.ErrorCode;
import com.example.be.score.dto.ScoreResponse;
import com.example.be.score.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/score")
@RequiredArgsConstructor
@Tag(name = "점수 기능", description = "점수 조회 기능을 포함한다.")
public class ScoreRestController {
    private final ScoreService scoreService;

    @Operation(summary = "점수 조회 기능",
            description = "사용자의 점수 목록을 조회한다.")
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.UNAUTHORIZED})
    @GetMapping()
    public ResponseEntity<List<ScoreResponse>> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(scoreService.getScores(userDetails.getUser()));
    }
}
