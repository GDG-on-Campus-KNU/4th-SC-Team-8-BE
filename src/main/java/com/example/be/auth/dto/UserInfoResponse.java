package com.example.be.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserInfoResponse(@Schema(description = "사용자 이름", example = "현재 사용자 이름")String username,
                               @Schema(description = "사용자 이메일", example = "현재 사용자 이메일 주소")String email) {
}
