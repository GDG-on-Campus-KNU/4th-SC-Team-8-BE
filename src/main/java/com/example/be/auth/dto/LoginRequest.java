package com.example.be.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(@Schema(description = "사용자 이메일", example = "email1@gmail.com") String email,
                           @Schema(description = "사용자 비밀 번호", example = "1234") String password) {
}
