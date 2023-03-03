package com.itzroma.astrocornerapi.resource;

import com.itzroma.astrocornerapi.model.dto.*;
import com.itzroma.astrocornerapi.security.service.JwtService;
import com.itzroma.astrocornerapi.service.impl.DefaultAuthService;
import com.itzroma.astrocornerapi.service.impl.DefaultEmailVerificationTokenService;
import com.itzroma.astrocornerapi.service.impl.DefaultRefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthResource {

    private final DefaultAuthService defaultAuthService;
    private final DefaultEmailVerificationTokenService defaultEmailVerificationTokenService;
    private final JwtService jwtService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUn(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return new ResponseEntity<>(
                new UserDto(defaultAuthService.signUp(signUpRequestDto).getEmail()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token) {
        if (defaultEmailVerificationTokenService.validateEmailVerificationToken(token)) {
            return ResponseEntity.ok("User is verified");
        }
        return new ResponseEntity<>("Cannot verify user, invalid verification link", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/resendEmailVerificationToken")
    public ResponseEntity<String> resendEmailVerificationToken(@RequestParam("token") String old) {
        defaultEmailVerificationTokenService.resendEmailVerificationToken(old);
        return ResponseEntity.ok("Verification link is resend");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDto> signIn(@Valid @RequestBody SignInRequestDto signInRequestDto) {
        return new ResponseEntity<>(defaultAuthService.signIn(signInRequestDto), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return new ResponseEntity<>(jwtService.refreshToken(refreshTokenRequestDto), HttpStatus.OK);
    }
}
