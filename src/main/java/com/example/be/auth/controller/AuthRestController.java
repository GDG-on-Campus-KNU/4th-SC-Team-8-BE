package com.example.be.auth.controller;

import com.example.be.auth.dto.*;
import com.example.be.auth.service.UserService;
import com.example.be.common.annotation.ApiErrorCodeExamples;
import com.example.be.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "인증/인가 기능", description = "회원 인증 및 인가 기능을 위한 api")
public class AuthRestController {
    private final UserService userService;

    @Operation(summary = "회원 가입 기능",
            description = "email, username, password를 입력받아 회원가입을 진행한다.",
            security = @SecurityRequirement(name = "jwt 제외"))
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.DUPLICATE_EMAIL, ErrorCode.DUPLICATE_USERNAME})
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "로그인 기능",
            description = "email, password를 입력받아 로그인을 진행한다.",
            security = @SecurityRequirement(name = "jwt 제외"))
    @ApiResponse(responseCode = "200", description = "로그인 성공 시 access token 및 refresh token이 발급된다.")
    @ApiErrorCodeExamples({ErrorCode.LOGIN_FAIL})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(loginRequest, request, response));
    }

    @Operation(summary = "refresh token 재발급 기능",
            description = "refresh token을 입력받아 토큰을 재발급 받는다.",
            security = @SecurityRequirement(name = "jwt 제외"))
    @ApiResponse(responseCode = "200", description = "access token 및 refresh token이 발급된다.")
    @ApiErrorCodeExamples({ErrorCode.INVALID_REFRESH_TOKEN})
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        return ResponseEntity.ok(userService.refresh(refreshRequest));
    }

    @Operation(summary = "구글 로그인을 통한 코드 요청",
            description = "구글 서버로부터 로그인을 하여 code를 발급받는다.(리다이렉트 형태)",
            security = @SecurityRequirement(name = "jwt 제외"))
    @ApiResponse(responseCode = "200", description = "code가 발급된다.")
    @GetMapping("/google-code")
    public ResponseEntity<?> getGoogleLoginURI(HttpServletRequest request) throws URISyntaxException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(userService.getGoogleLoginURI(request)));
        return new ResponseEntity<>(httpHeaders, HttpStatus.PERMANENT_REDIRECT);
    }

    @Operation(summary = "구글 로그인",
            description = "구글 서버로부터 받은 코드를 입력값으로 넣어 백엔드 서버에 로그인을 진행한다.",
            security = @SecurityRequirement(name = "jwt 제외"))
    @ApiResponse(responseCode = "200", description = "access token 및 refresh token이 발급된다.")
    @ApiErrorCodeExamples({ErrorCode.LOGIN_FAIL})
    @PostMapping("/google-login")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.googleLogin(googleLoginRequest.code(), request));
    }
}
