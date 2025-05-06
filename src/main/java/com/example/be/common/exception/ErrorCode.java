package com.example.be.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //OK
    OK(HttpStatus.OK, "성공했습니다."),

    //Auth
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 있습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "중복된 사용자명이 있습니다."),
    LOGIN_FAIL(HttpStatus.CONFLICT, "로그인에 실패했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었거나 로그인을 하지 않았습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),

    //Game
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "게임을 찾을 수 없습니다."),
    GAME_DUPLICATE_YOUTUBE_LINK(HttpStatus.CONFLICT, "중복된 유튜브 링크가 있습니다."),
    FAIL_SEND_EMAIL(HttpStatus.CONFLICT, "메일 전송에 실패했습니다."),

    //Gemini
    GEMINI_API_ERROR(HttpStatus.CONFLICT, "gemini 호출 중 에러가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable throwable) {
        return this.getMessage(this.getMessage(this.getMessage() + " - " + throwable.getMessage()));
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
}
