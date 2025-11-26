package com.example.servicea.domain.home;

import com.example.servicea.global.config.exception.CustomException;
import com.example.servicea.global.config.exception.CustomExceptionEnum;
import com.example.servicea.global.support.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        String userId = UserContext.getUserId();
        log.info("Current UserId: " + userId);
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
}
