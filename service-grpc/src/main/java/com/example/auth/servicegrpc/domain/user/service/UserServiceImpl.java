package com.example.auth.servicegrpc.domain.user.service;

import com.example.auth.servicegrpc.UserRequest;
import com.example.auth.servicegrpc.UserResponse;
import com.example.auth.servicegrpc.UserServiceGrpc;
import com.example.auth.servicegrpc.domain.user.entity.UserEntity;
import com.example.auth.servicegrpc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;
    @Override
    public void getUser(UserRequest request, io.grpc.stub.StreamObserver<UserResponse> responseObserver) {
        // DB에서 조회
        UserEntity user = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // gRPC 응답 생성
        UserResponse response = UserResponse.newBuilder()
                .setId(user.getId().intValue())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
