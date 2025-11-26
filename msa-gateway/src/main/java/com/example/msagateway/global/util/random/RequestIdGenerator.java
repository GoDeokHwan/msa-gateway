package com.example.msagateway.global.util.random;

import java.security.SecureRandom;

public class RequestIdGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    public static String generate() {
        long now = System.currentTimeMillis() % 1_000_000_000L;  // 9자리
        int rnd = random.nextInt(10_000);                        // 0~9999 난수

        long combined = now * 10_000 + rnd;                      // 시간 + 난수 결합
        return toBase62(combined);                               // Base62 변환
    }

    private static String toBase62(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62[(int) (value % 62)]);
            value /= 62;
        }
        return sb.reverse().toString();
    }
}
