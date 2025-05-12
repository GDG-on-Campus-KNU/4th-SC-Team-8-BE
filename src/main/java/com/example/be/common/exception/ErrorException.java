package com.example.be.common.exception;

public class ErrorException extends RuntimeException {
    ErrorCode errorCode;
    public ErrorException(String message) {
        super(message);
    }
    public ErrorException(ErrorCode errorcode){this.errorCode = errorcode;}
}
