package com.itzroma.astrocornerapi.resource;

import com.itzroma.astrocornerapi.aop.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ForgotPasswordResource {
    @PostMapping("/forgot-password")
    @RateLimiter(key = "#email", permits = 3)
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        log.info("forgot {}", email);
        return ResponseEntity.ok().build();
    }
}
