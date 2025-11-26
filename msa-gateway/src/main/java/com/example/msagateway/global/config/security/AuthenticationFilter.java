package com.example.msagateway.global.config.security;

import com.example.msagateway.global.config.binder.WebProperty;
import com.example.msagateway.global.util.random.RequestIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
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
        String requestId = RequestIdGenerator.generate();
        long startTime = System.currentTimeMillis();
        MDC.put("requestId", requestId);
        // Reactor Context에 requestId 저장
        return chainFilter(exchange, chain, requestId, startTime)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }

    private Mono<Void> chainFilter(ServerWebExchange exchange, GatewayFilterChain chain, String requestId, long startTime) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("[Gateway] Request Path: {}", path);

        // JWT 검증 없이 통과
        for (String pattern : webProperty.getSecurity()) {
            if (pathMatcher.match(pattern, path)) {
                return chain.filter(exchange)
                        .doFinally(sig -> logDuration(exchange, path, startTime, requestId));
            }
        }

        HttpCookie accessToken = exchange.getRequest().getCookies().getFirst("ACCESS_TOKEN");
        if (accessToken == null) return unauthorized(exchange);

        if (!jwtValidator.validate(accessToken.getValue())) {
            return refresh(exchange, chain, startTime, requestId);
        }

        ServerWebExchange mutated = injectUserId(exchange, accessToken.getValue(), requestId);
        return chain.filter(mutated)
                .doFinally(sig -> logDuration(exchange, path, startTime, requestId));
    }

    private void logDuration(ServerWebExchange exchange, String path, long startTime, String requestId) {
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String targetPath = routeUri != null ? routeUri.getPath() : path;
        long duration = System.currentTimeMillis() - startTime;
        MDC.put("requestId", requestId);
        log.info("[Gateway] Response Path: {}, Duration: {} ms, requestId={}", targetPath, duration, requestId);
    }

    private ServerWebExchange injectUserId(ServerWebExchange exchange, String token, String requestId) {
        String userId = jwtValidator.getUserId(token);
        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .header("X-USER-ID", userId)
                .header("X-Request-Id", requestId)
                .build();
        return exchange.mutate().request(mutated).build();
    }

    private Mono<Void> refresh(ServerWebExchange exchange, GatewayFilterChain chain, long startTime, String requestId) {
        HttpCookie refreshToken = exchange.getRequest().getCookies().getFirst("REFRESH_TOKEN");
        if (refreshToken == null) return unauthorized(exchange);

        return authClient.post()
                .uri("/auth/refresh")
                .bodyValue(Map.of("refreshToken", refreshToken.getValue()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(tokens -> {
                    exchange.getResponse().addCookie(createAccessCookie(tokens));
                    ServerWebExchange mutated = injectUserId(exchange, tokens, requestId);
                    return chain.filter(mutated)
                            .doFinally(sig -> logDuration(exchange, null, startTime, requestId));
                });
    }

    private ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("ACCESS_TOKEN", token)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();
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
