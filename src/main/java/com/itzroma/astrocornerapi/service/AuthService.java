package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.email.event.EmailVerificationEvent;
import com.itzroma.astrocornerapi.exception.ReAuthenticationRequiredException;
import com.itzroma.astrocornerapi.model.dto.AuthResponse;
import com.itzroma.astrocornerapi.model.dto.RefreshTokenRequest;
import com.itzroma.astrocornerapi.model.dto.SignInRequest;
import com.itzroma.astrocornerapi.model.dto.SignUpRequest;
import com.itzroma.astrocornerapi.model.entity.EmailVerificationToken;
import com.itzroma.astrocornerapi.model.entity.RefreshToken;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.security.service.JwtService;
import com.itzroma.astrocornerapi.security.userdetails.JwtUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final ApplicationEventPublisher publisher;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public User signUp(SignUpRequest signUpRequest, HttpServletRequest request) {
        User user = userService.save(new User(signUpRequest.email(), signUpRequest.password()));
        EmailVerificationToken evt = emailVerificationTokenService.generateEmailVerificationToken(user);
        sendEmailRegistrationToken(request, user, evt);
        return user;
    }

    private void sendEmailRegistrationToken(HttpServletRequest request, User user, EmailVerificationToken evt) {
        publisher.publishEvent(new EmailVerificationEvent(user, applicationUrl(request), evt));
    }

    private String applicationUrl(HttpServletRequest request) {
        return "%s://%s/%sauth/".formatted(
                request.getScheme(),
                request.getServerName() + ":" + request.getServerPort(),
                request.getContextPath()
        );
    }

    public boolean resendEmailVerificationToken(String old, HttpServletRequest request) {
        try {
            EmailVerificationToken evt = emailVerificationTokenService.regenerateEmailVerificationToken(old);
            sendEmailRegistrationToken(request, evt.getUser(), evt);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional
    public AuthResponse signIn(SignInRequest signInRequest) {
        User user = userService.findByEmail(signInRequest.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), signInRequest.password()
        ));
        return generateAuthResponseFromUser(user);
    }

    private AuthResponse generateAuthResponseFromUser(User user) {
        JwtUserDetails jwtUserDetails = JwtUserDetails.fromUser(user);
        String accessToken = jwtService.generateAccessToken(jwtUserDetails);
        String refreshToken = jwtService.generateRefreshToken(jwtUserDetails);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.refreshToken());
        if (jwtService.validateRefreshToken(refreshToken.getToken())) {
            String accessToken = jwtService.generateAccessToken(JwtUserDetails.fromUser(refreshToken.getUser()));
            return new AuthResponse(accessToken, refreshToken.getToken());
        }
        throw new ReAuthenticationRequiredException();
    }
}
