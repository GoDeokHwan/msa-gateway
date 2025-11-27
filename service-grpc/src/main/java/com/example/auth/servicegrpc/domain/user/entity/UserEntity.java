package com.example.auth.servicegrpc.domain.user.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserEntity {
    private Long id;
    private String name;
    private String email;
}
