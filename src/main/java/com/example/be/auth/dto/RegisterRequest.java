package com.example.be.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequest(@Schema(description = "사용자 이메일", example = "email1@gmail.com") String email,
                              @Schema(description = "사용자 이름", example = "무작위 이름 1") String username,
                              @Schema(description = "사용자 비밀 번호", example = "1234") String password) {
}
