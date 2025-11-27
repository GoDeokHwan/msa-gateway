package com.example.auth.servicegrpc.domain.user.repository;

import com.example.auth.servicegrpc.domain.user.entity.UserEntity;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MockUserRepository implements UserRepository {
    private final Faker faker = new Faker();

    @Override
    public Optional<UserEntity> findById(Long id) {
        UserEntity user = UserEntity.builder()
                .id(id)
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .build();
        return Optional.of(user);
    }
}
