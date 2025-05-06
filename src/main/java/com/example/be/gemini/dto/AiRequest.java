package com.example.be.gemini.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AiRequest(@Schema(description = "질문", example = "## 단어의 수어 동작을 알려줘.") String text) {
}
