package com.example.auth.servicegrpc.global.config.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GrpcPortLogger implements CommandLineRunner {
    private final Environment env;
    @Override
    public void run(String... args) throws Exception {
        String grpcPort = env.getProperty("grpc.server.port", "9090");
        log.info("gRPC should be running on port: {}", grpcPort);
    }
}
