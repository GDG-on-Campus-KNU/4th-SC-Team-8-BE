package com.example.be.common.exception;

public class NotFoundException extends RuntimeException {
    ErrorCode errorCode;
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(ErrorCode errorcode){this.errorCode = errorcode;}
}
