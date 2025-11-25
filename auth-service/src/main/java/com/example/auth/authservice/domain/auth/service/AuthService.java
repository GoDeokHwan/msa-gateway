package com.example.auth.authservice.domain.auth.service;

import com.example.auth.authservice.domain.auth.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    public UserDTO login(String username, String password) {
        if (username == null || password == null) {
            throw new RuntimeException("Username and/or password null");
        }
        String testEncoded = "$2a$10$rihxEb/buvAFO8ElTUJMbuLHtuF/jzMKkGzuSTSKjmddsxoZQrhku"; // 테스트용

        if (!"username".equals(username)
                || !passwordEncoder.matches(password, testEncoded)) {
            throw new RuntimeException("Invalid username or password");
        }
        return UserDTO.of(1L, "test", "username");
    }


}
