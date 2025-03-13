package com.example.be.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private String code;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.code = errorCode.getHttpStatus().toString();
        this.message = errorCode.getMessage();
    }
}
