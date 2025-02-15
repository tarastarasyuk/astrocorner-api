package com.itzroma.astrocornerapi.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class TestResource {
    @GetMapping("/all")
    public ResponseEntity<String> all() {
        return ResponseEntity.ok("hello all");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authenticated")
    public ResponseEntity<String> authenticated(Principal principal) {
        return ResponseEntity.ok("hello authenticated"+principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-profile")
    public ResponseEntity<String> myProfile(Principal principal) {
        return ResponseEntity.ok("myProfile "+principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/register-additional")
    public ResponseEntity<String> registerStepTwo(Principal principal) {
        return ResponseEntity.ok("registerStepTwo "+principal.getName());
    }
}
