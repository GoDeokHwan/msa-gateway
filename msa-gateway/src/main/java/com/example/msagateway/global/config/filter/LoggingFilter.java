package com.example.msagateway.global.config.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        log.info("[Gateway] Request Path: {}", path);

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
                    String targetPath = routeUri != null ? routeUri.getPath() : path;
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("[Gateway] Response Path: {}, Duration: {} ms", targetPath, duration);
                });
    }

    @Override
    public int getOrder() {
        return 1; // 가장 먼저 실행
    }
}