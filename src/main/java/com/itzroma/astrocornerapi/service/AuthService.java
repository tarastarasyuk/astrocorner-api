package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.model.dto.AuthResponse;
import com.itzroma.astrocornerapi.model.dto.SignInRequest;
import com.itzroma.astrocornerapi.model.dto.SignUpRequest;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.security.service.JwtService;
import com.itzroma.astrocornerapi.security.userdetails.JwtUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse signUp(SignUpRequest signUpRequest) {
        User user = userService.save(new User(signUpRequest.email(), signUpRequest.password()));
        return generateAuthResponseFromUser(user);
    }

    private AuthResponse generateAuthResponseFromUser(User user) {
        JwtUserDetails jwtUserDetails = JwtUserDetails.fromUser(user);
        String accessToken = jwtService.generateAccessToken(jwtUserDetails);
        String refreshToken = jwtService.generateRefreshToken(jwtUserDetails);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.email(), signInRequest.password()
        ));
        User user = userService.findByEmail(signInRequest.email());
        return generateAuthResponseFromUser(user);
    }
}
