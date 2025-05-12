package com.example.be.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ProblemDetail> handleErrorException(ErrorException e) {
        ErrorCode errorCode = e.errorCode;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorCode.getHttpStatus(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(problemDetail);
    }
}
