package com.example.msagateway.global.config.security;

import com.example.msagateway.global.config.binder.WebProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter implements GlobalFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final WebProperty webProperty;
    private final JwtValidator  jwtValidator;
    private final WebClient authClient;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        log.info("[Gateway] Request Path: {}", path);
        // JWT 검증 없이 통과시킬 경로
        for (String pattern : webProperty.getSecurity()) {
            if (pathMatcher.match(pattern, path)) {
                return chain.filter(exchange)
                        .doOnSuccess(aVoid -> {
                            URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
                            String targetPath = routeUri != null ? routeUri.getPath() : path;
                            long duration = System.currentTimeMillis() - startTime;
                            log.info("[Gateway] Response Path: {}, Duration: {} ms", targetPath, duration);
                        });
            }
        }

        HttpCookie accessToken = exchange.getRequest().getCookies().getFirst("ACCESS_TOKEN");

        if (accessToken == null) {
            return unauthorized(exchange);
        }

        if (!jwtValidator.validate(accessToken.getValue())) {
            return refresh(exchange, chain);
        }
        // JWT에서 User ID 추출
        String userId = jwtValidator.getUserId(accessToken.getValue());
        // Request 헤더에 추가
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-USER-ID", userId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        return chain.filter(mutatedExchange)
                .doOnSuccess(aVoid -> logDuration(exchange, path, startTime));
    }
    private void logDuration(ServerWebExchange exchange, String path, long startTime) {
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String targetPath = routeUri != null ? routeUri.getPath() : path;
        long duration = System.currentTimeMillis() - startTime;
        log.info("[Gateway] Response Path: {}, Duration: {} ms", targetPath, duration);
    }

    private Mono<Void> refresh(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpCookie refreshToken = exchange.getRequest().getCookies().getFirst("REFRESH_TOKEN");
        if (refreshToken == null) {
            return unauthorized(exchange);
        }


        return authClient.post()
                .uri("/auth/refresh")
                .bodyValue(Map.of("refreshToken", refreshToken.getValue()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(tokens -> {
                    // 새 토큰 쿠키 생성
                    ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", tokens)
                            .httpOnly(true)
                            .path("/")
                            .maxAge(Duration.ofHours(1))
                            .build();

                    // Response에 추가
                    exchange.getResponse().addCookie(accessCookie);

                    // JWT에서 User ID 추출
                    String userId = jwtValidator.getUserId(tokens);
                    // Request 헤더에 추가
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-USER-ID", userId)
                            .build();

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(mutatedRequest)
                            .build();

                    // 원래 요청 계속 진행
                    return chain.filter(mutatedExchange);
                });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"error\": \"Unauthorized\"}";
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

}
