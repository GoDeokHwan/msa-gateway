package com.example.auth.servicegrpc.domain.user.repository;

import com.example.auth.servicegrpc.domain.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findById(Long id);
}
