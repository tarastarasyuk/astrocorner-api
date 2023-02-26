package com.itzroma.astrocornerapi.resource;

import com.itzroma.astrocornerapi.model.dto.*;
import com.itzroma.astrocornerapi.service.AuthService;
import com.itzroma.astrocornerapi.service.EmailVerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthResource {

    private final AuthService authService;

    private final EmailVerificationTokenService emailVerificationTokenService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUn(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
        return new ResponseEntity<>(
                new UserDto(authService.signUp(signUpRequest, request).getEmail()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token) {
        if (emailVerificationTokenService.validateEmailVerificationToken(token)) {
            return ResponseEntity.ok("User is verified");
        }
        return new ResponseEntity<>("Cannot verify user, invalid verification link", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/resendEmailVerificationToken")
    public ResponseEntity<String> resendEmailVerificationToken(@RequestParam("token") String old,
                                                               HttpServletRequest request) {
        if (authService.resendEmailVerificationToken(old, request)) {
            return ResponseEntity.ok("Verification link is resend");
        }
        return new ResponseEntity<>("Cannot resend verification link", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return new ResponseEntity<>(authService.signIn(signInRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(authService.refreshToken(refreshTokenRequest), HttpStatus.OK);
    }
}
