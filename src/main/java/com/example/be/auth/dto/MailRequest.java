package com.example.be.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MailRequest(@Schema(description = "유튜브 주소", example = "https://www.youtube.com/watch?v=EhJzbcbz5-s")String url,
                          @Schema(description = "변환 여부", example = "SUCCESS 또는 FAIL")MailEnum status) {
}
