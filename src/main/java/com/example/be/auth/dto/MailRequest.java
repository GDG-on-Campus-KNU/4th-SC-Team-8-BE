package com.example.be.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MailRequest(@Schema(description = "유튜브 주소", example = "https://www.youtube.com/watch?v=EhJzbcbz5-s")String url) {
}
