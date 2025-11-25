package com.example.servicea.global.config.filter;

import com.example.servicea.global.support.user.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String userId = req.getHeader("X-USER-ID");
        if (userId != null) {
            UserContext.setUserId(userId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear(); // 요청 종료 후 반드시 제거
        }
    }
}
