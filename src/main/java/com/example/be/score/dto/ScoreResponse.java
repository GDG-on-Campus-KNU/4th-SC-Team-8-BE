package com.example.be.score.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ScoreResponse(@Schema(description = "유튜브 링크", example = "https://www.youtube.com/watch?v=G0CVDkc5M0M") String youtubeLink,
                            @Schema(description = "점수", example = "312")long score) {
}
