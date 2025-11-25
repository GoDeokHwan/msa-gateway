package com.example.auth.authservice.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}
