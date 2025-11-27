package com.example.servicea.global.config.exception;

import com.example.servicea.global.util.date.LocalDateTimeUtil;
import lombok.Getter;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@Getter
public class ErrorMessageResponse {
    private String code;
    private String message;
    private String requestId;
    private LocalDateTime timestamp;

    public static ErrorMessageResponse error(CustomException exception) {
        ErrorMessageResponse response = new ErrorMessageResponse();
        response.code = exception.getException().name();
        response.message = exception.getMessage();
        response.timestamp = LocalDateTimeUtil.kstTimeNot();
        // MDC 키 일관성: "requestId" 또는 "traceId" 둘 다 확인 (Gateway/서비스마다 다를 수 있으므로)
        response.requestId = MDC.get("requestId");
        if (response.requestId == null) {
            response.requestId = MDC.get("traceId");
        }
        // fallback: 짧은 id 또는 빈 문자열(원하면 UUID 사용)
        if (response.requestId == null) {
            response.requestId = ""; // 또는 RequestIdGenerator.generate()
        }
        return response;
    }

}
