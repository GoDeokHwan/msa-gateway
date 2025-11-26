package com.example.servicea.global.config.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private CustomExceptionEnum exception;
    private String message;

    public CustomException(CustomExceptionEnum exception) {
        this.exception = exception;
    }

    public CustomException(CustomExceptionEnum exception, String message) {
        this.exception = exception;
        this.message = message;
    }

    public String getMessage() {
        if (message == null)
            return exception.getMessage();
        return message;
    }

}
