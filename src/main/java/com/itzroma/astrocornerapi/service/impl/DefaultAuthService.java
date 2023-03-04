package com.itzroma.astrocornerapi.service.impl;

import com.itzroma.astrocornerapi.model.dto.AuthResponseDto;
import com.itzroma.astrocornerapi.model.dto.RegisterRequestDto;
import com.itzroma.astrocornerapi.model.dto.SignInRequestDto;
import com.itzroma.astrocornerapi.model.entity.AuthProvider;
import com.itzroma.astrocornerapi.model.entity.EmailVerificationToken;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.security.service.JwtService;
import com.itzroma.astrocornerapi.security.userdetails.DefaultUserDetails;
import com.itzroma.astrocornerapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {

    private final DefaultUserService defaultUserService;
    private final DefaultEmailVerificationTokenService defaultEmailVerificationTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public User standardSignUp(RegisterRequestDto registerRequestDto) {
        User user = new User(registerRequestDto.email(), registerRequestDto.password());

        user.setLastName(registerRequestDto.lastName());
        user.setFirstName(registerRequestDto.firstName());
        fulfillUserCommonData(user, registerRequestDto);

        User registeredUser = defaultUserService.save(user);

        EmailVerificationToken evt = defaultEmailVerificationTokenService.generateEmailVerificationToken(registeredUser);
        defaultEmailVerificationTokenService.sendEmailRegistrationToken(registeredUser, evt);

        return registeredUser;
    }

    public User oauth2SignUp(RegisterRequestDto registerRequestDto, Authentication authentication) {
        DefaultUserDetails defaultUserDetails = (DefaultUserDetails) authentication.getPrincipal();
        User user = defaultUserService.findByEmail(defaultUserDetails.getEmail());

        fulfillUserCommonData(user, registerRequestDto);

        return defaultUserService.update(user);
    }

    private void fulfillUserCommonData(User user, RegisterRequestDto registerRequestDto) {
        user.setProvider(AuthProvider.valueOf(registerRequestDto.authProvider()));
        user.setInfo(registerRequestDto.info());
    }

    @Override
    public AuthResponseDto signIn(SignInRequestDto signInRequestDto) {
        User user = defaultUserService.findByEmail(signInRequestDto.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), signInRequestDto.password()
        ));
        return generateAuthResponseFromUser(DefaultUserDetails.fromUser(user));
    }

    public AuthResponseDto generateAuthResponseFromUser(DefaultUserDetails defaultUserDetails) {
        String accessToken = jwtService.generateAccessToken(defaultUserDetails);
        String refreshToken = jwtService.generateRefreshToken(defaultUserDetails);
        return new AuthResponseDto(accessToken, refreshToken);
    }

}
