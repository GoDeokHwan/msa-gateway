package com.example.servicea.global.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        CustomException ce = new CustomException(CustomExceptionEnum.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessageResponse.error(ce));
    }

    @ExceptionHandler(CustomException.class)
    private ResponseEntity<ErrorMessageResponse> handleCustomException(CustomException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(e.getException().getStatus())
                .body(ErrorMessageResponse.error(e))
                ;
    }

}
