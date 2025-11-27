package com.example.servicea.domain.home;

import com.example.servicea.global.binder.VaultProperties;
import com.example.servicea.global.config.exception.CustomException;
import com.example.servicea.global.config.exception.CustomExceptionEnum;
import com.example.servicea.global.support.user.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeController {

    private final VaultProperties vaultProperties;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        String userId = UserContext.getUserId();
        log.info("Current UserId: " + userId);
        log.info("Current Api Key: " + vaultProperties.getApiKey());
        log.info("Current Api Url: " + vaultProperties.getApiUrl());
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/save")
    public ResponseEntity<HomeRequest> post(@RequestBody HomeRequest homeRequest) {
        String userId = UserContext.getUserId();
        log.info("Current UserId: " + userId);
        return ResponseEntity.ok(new HomeRequest(userId, homeRequest.name(), homeRequest.email()));
    }

    @PostMapping("/exception")
    public ResponseEntity<Void> exception() {
        if (true) {
            throw new CustomException(CustomExceptionEnum.FAIL_MESSAGE);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile file) {
        String userId = UserContext.getUserId();
        log.info("Current UserId: " + userId);
        log.info(file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename());
    }
}
