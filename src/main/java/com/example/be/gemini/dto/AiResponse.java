package com.example.be.gemini.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AiResponse(@Schema(description = "답변", example = "## 동작은 ~~") String text) {
}
