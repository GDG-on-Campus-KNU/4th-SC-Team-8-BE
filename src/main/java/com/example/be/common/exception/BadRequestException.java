package com.example.be.common.exception;

public class BadRequestException extends RuntimeException {
    ErrorCode errorCode;
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(ErrorCode errorcode){this.errorCode = errorcode;}
}
