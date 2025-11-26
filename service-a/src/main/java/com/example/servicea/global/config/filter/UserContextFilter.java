package com.example.servicea.global.config.filter;

import com.example.servicea.global.support.user.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String userId = req.getHeader("X-USER-ID");
        String requestId = req.getHeader("X-Request-Id");
        if (userId != null) {
            UserContext.setUserId(userId);
        }

        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString().replace("-", "");
        }

        // 로그 MDC 세팅
        MDC.put("requestId", requestId);

        // 예외 처리기에서 사용할 수 있게 저장
        request.setAttribute("requestId", requestId);

        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear(); // 요청 종료 후 반드시 제거
            MDC.clear();
        }
    }
}
