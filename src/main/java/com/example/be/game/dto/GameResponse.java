package com.example.be.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GameResponse(@Schema(description = "변환할 유튜브 링크 주소", example = "https://www.youtube.com/watch?v=G0CVDkc5M0M")String youtubeLink) {
}
