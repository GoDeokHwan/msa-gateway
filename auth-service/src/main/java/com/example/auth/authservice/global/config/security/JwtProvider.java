package com.example.auth.authservice.global.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {
    private static final String SECRET = "yp7HOHEZtkO7hczRae+HnjSn0gditnvC/Yms/xjGQFI=";

    public String createAccessToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(1))))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public String createRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(Date.from(Instant.now().plus(Duration.ofDays(7))))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
