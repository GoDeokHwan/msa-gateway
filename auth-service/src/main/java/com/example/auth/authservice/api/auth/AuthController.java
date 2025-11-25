package com.example.auth.authservice.api.auth;

import com.example.auth.authservice.api.auth.dto.LoginRequest;
import com.example.auth.authservice.api.auth.dto.RefreshRequest;
import com.example.auth.authservice.domain.auth.dto.UserDTO;
import com.example.auth.authservice.domain.auth.service.AuthService;
import com.example.auth.authservice.global.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(
            @RequestBody LoginRequest request
    ) {
        // 로그인 확인 로직
        UserDTO user = authService.login(request.email(), request.password());

        String accessToken = jwtProvider.createAccessToken(user.getId().toString());
        String refreshToken = jwtProvider.createRefreshToken(user.getId().toString());


        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                .httpOnly(true)
                .path("/")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
         @RequestBody RefreshRequest request
    ) {
        String refreshToken = request.refreshToken();
        if (refreshToken == null || !jwtProvider.validate(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtProvider.getClaims(refreshToken).getSubject();

//        if (!refreshStore.validate(username, refreshToken)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        String newAccessToken = jwtProvider.createAccessToken(username);

        return ResponseEntity.ok()
                .body(newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody RefreshRequest request) {
        String refreshToken = request.refreshToken();
        if (refreshToken != null && jwtProvider.validate(refreshToken)) {
            String username = jwtProvider.getClaims(refreshToken).getSubject();
//            refreshStore.remove(username);
        }

        ResponseCookie clearAccess = ResponseCookie.from("ACCESS_TOKEN", "")
                .maxAge(0)  // 즉시 삭제
                .path("/")
                .build();

        ResponseCookie clearRefresh = ResponseCookie.from("REFRESH_TOKEN", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearAccess.toString())
                .header(HttpHeaders.SET_COOKIE, clearRefresh.toString())
                .body("logout success");
    }
}
