package com.itzroma.astrocornerapi.service.impl;

import com.itzroma.astrocornerapi.model.dto.AuthResponseDto;
import com.itzroma.astrocornerapi.model.dto.SignInRequestDto;
import com.itzroma.astrocornerapi.model.dto.SignUpRequestDto;
import com.itzroma.astrocornerapi.model.entity.EmailVerificationToken;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.security.service.JwtService;
import com.itzroma.astrocornerapi.security.userdetails.DefaultUserDetails;
import com.itzroma.astrocornerapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {

    private final DefaultUserService defaultUserService;
    private final DefaultEmailVerificationTokenService defaultEmailVerificationTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public User signUp(SignUpRequestDto signUpRequestDto) {
        User user = defaultUserService.save(new User(signUpRequestDto.email(), signUpRequestDto.password()));
        EmailVerificationToken evt = defaultEmailVerificationTokenService.generateEmailVerificationToken(user);
        defaultEmailVerificationTokenService.sendEmailRegistrationToken(user, evt);
        return user;
    }

    @Override
    public AuthResponseDto signIn(SignInRequestDto signInRequestDto) {
        User user = defaultUserService.findByEmail(signInRequestDto.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), signInRequestDto.password()
        ));
        return generateAuthResponseFromUser(user);
    }

    private AuthResponseDto generateAuthResponseFromUser(User user) {
        DefaultUserDetails defaultUserDetails = DefaultUserDetails.fromUser(user);
        String accessToken = jwtService.generateAccessToken(defaultUserDetails);
        String refreshToken = jwtService.generateRefreshToken(defaultUserDetails);
        return new AuthResponseDto(accessToken, refreshToken);
    }

}
