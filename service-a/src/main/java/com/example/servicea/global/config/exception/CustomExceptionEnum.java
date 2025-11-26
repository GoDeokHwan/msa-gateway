package com.example.servicea.global.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomExceptionEnum {
    SUCCESS(HttpStatus.OK, "success", true)

    , INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error. Please try again.", false)
    , FAIL_MESSAGE(HttpStatus.BAD_REQUEST, "fail Message", true)
    ;

    private HttpStatus status;
    private String message;
    private boolean shotFlag;
}
