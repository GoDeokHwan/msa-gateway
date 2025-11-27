package com.example.auth.servicegrpc;

import com.example.auth.servicegrpc.domain.user.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class ServiceGrpcApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ServiceGrpcApplication.class, args);
    }

}
