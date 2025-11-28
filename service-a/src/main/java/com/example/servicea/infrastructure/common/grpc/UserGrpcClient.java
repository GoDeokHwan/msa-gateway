package com.example.servicea.infrastructure.common.grpc;

import com.example.auth.servicea.UserRequest;
import com.example.auth.servicea.UserResponse;
import com.example.auth.servicea.UserServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Component;

@Component
public class UserGrpcClient {
    private final UserServiceGrpc.UserServiceBlockingStub stub;

    public UserGrpcClient(ManagedChannel channel) {
        this.stub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserResponse getUser(Long userId) {
        var request = UserRequest.newBuilder()
                .setId(userId)
                .build();

        return stub.getUser(request);
    }
}
