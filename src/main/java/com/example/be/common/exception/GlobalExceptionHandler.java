package com.example.be.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ProblemDetail> handleForbiddenException(ForbiddenException e) {
        ErrorCode errorCode = e.errorCode;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, errorCode.getMessage());
        problemDetail.setTitle("Forbidden");
        return ResponseEntity.status(errorCode.getHttpStatus()).body(problemDetail);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflictException(ConflictException e) {
        ErrorCode errorCode = e.errorCode;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, errorCode.getMessage());
        problemDetail.setTitle("Conflict");
        return ResponseEntity.status(errorCode.getHttpStatus()).body(problemDetail);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(NotFoundException e) {
        ErrorCode errorCode = e.errorCode;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, errorCode.getMessage());
        problemDetail.setTitle("Not Found");
        return ResponseEntity.status(errorCode.getHttpStatus()).body(problemDetail);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedException(UnauthorizedException e) {
        ErrorCode errorCode = e.errorCode;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, errorCode.getMessage());
        problemDetail.setTitle("Unauthorized");
        return ResponseEntity.status(errorCode.getHttpStatus()).body(problemDetail);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException e) {
        ErrorCode errorCode = e.errorCode;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorCode.getMessage());
        problemDetail.setTitle("Bad Request");
        return ResponseEntity.status(errorCode.getHttpStatus()).body(problemDetail);
    }
}
