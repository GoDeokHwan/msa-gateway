package com.example.servicea.infrastructure.common.grpc;

import com.example.auth.servicea.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel userGrpcChannel() {
        return ManagedChannelBuilder
                .forAddress("localhost", 9091)
                .usePlaintext()
                .build();
    }


    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(ManagedChannel userChannel) {
        return UserServiceGrpc.newBlockingStub(userChannel);
    }
}
