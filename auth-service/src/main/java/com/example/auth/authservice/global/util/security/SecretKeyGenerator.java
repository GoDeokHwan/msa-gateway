package com.example.auth.authservice.global.util.security;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    private static final int KEY_LENGTH = 32; // 32 bytes = 256bit

    public static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[KEY_LENGTH];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
    public static void main(String[] args) {
        System.out.println(generateSecretKey());
    }
}
