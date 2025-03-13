package com.example.be.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshRequest(@Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.asdoiqwdqs.sdqdeqdq") String refreshToken) {
}
