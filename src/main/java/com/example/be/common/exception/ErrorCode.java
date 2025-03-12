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
    DUPLICATE_NICK_NAME(HttpStatus.CONFLICT, "중복된 닉네임이 있습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 시 입력한 정보가 틀렸거나, 로그인을 하지 않았습니다."),

    //Game
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "게임을 찾을 수 없습니다.")
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
