package com.itzroma.astrocornerapi.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.itzroma.astrocornerapi.exception.EntityNotFoundException;
import com.itzroma.astrocornerapi.exception.ReAuthenticationRequiredException;
import com.itzroma.astrocornerapi.model.dto.AuthResponseDto;
import com.itzroma.astrocornerapi.model.dto.RefreshTokenRequestDto;
import com.itzroma.astrocornerapi.model.entity.RefreshToken;
import com.itzroma.astrocornerapi.model.entity.User;
import com.itzroma.astrocornerapi.repository.UserRepository;
import com.itzroma.astrocornerapi.security.userdetails.DefaultUserDetails;
import com.itzroma.astrocornerapi.service.impl.DefaultRefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtService {
    private final Algorithm accessTokenAlgorithm;
    private final Algorithm refreshTokenAlgorithm;

    private HttpServletRequest request;
    private UserRepository userRepository;
    private DefaultRefreshTokenService defaultRefreshTokenService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRefreshTokenService(DefaultRefreshTokenService defaultRefreshTokenService) {
        this.defaultRefreshTokenService = defaultRefreshTokenService;
    }

    @Value("${app.security.jwt.access.expiration-ms}")
    private Long accessTokenExpirationMs;

    @Value("${app.security.jwt.refresh.expiration-ms}")
    private Long refreshTokenExpirationMs;

    public JwtService(@Value("${app.security.jwt.access.secret}") String accessSecret,
                      @Value("${app.security.jwt.refresh.secret}") String refreshSecret) {
        accessTokenAlgorithm = Algorithm.HMAC512(accessSecret);
        refreshTokenAlgorithm = Algorithm.HMAC512(refreshSecret);
    }

    public String generateAccessToken(DefaultUserDetails defaultUserDetails) {
        return JWT.create()
                .withIssuer(request.getRequestURL().toString())
                .withSubject(defaultUserDetails.getUsername())
                .withClaim("roles", defaultUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .sign(accessTokenAlgorithm);
    }

    public String generateRefreshToken(DefaultUserDetails defaultUserDetails) {
        User user = userRepository.findByEmail(defaultUserDetails.getUsername()).orElseThrow(() -> {
            throw new EntityNotFoundException("Cannot generate refresh token: user by email not found");
        });

        String token = JWT.create()
                .withIssuer(request.getRequestURL().toString())
                .withSubject(user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .sign(refreshTokenAlgorithm);

        return defaultRefreshTokenService.save(new RefreshToken(user, token)).getToken();
    }

    private boolean validateToken(String token, Algorithm algorithm) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (SignatureVerificationException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (TokenExpiredException ex) {
            log.error("JWT token is expired: {}", ex.getMessage());
            if (algorithm == refreshTokenAlgorithm) {
                defaultRefreshTokenService.deleteByToken(token);
                log.info("Expired JWT refresh token deleted");
            }
        } catch (JWTVerificationException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Unknown error: {}", ex.getMessage());
        }
        return false;
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessTokenAlgorithm);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshTokenAlgorithm);
    }

    public AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshToken refreshToken = defaultRefreshTokenService.findByToken(refreshTokenRequestDto.refreshToken());
        if (validateRefreshToken(refreshToken.getToken())) {
            String accessToken = generateAccessToken(DefaultUserDetails.fromUser(refreshToken.getUser()));
            return new AuthResponseDto(accessToken, refreshToken.getToken());
        }
        throw new ReAuthenticationRequiredException();
    }

    public String getSubjectFromAccessToken(String token) {
        JWTVerifier verifier = JWT.require(accessTokenAlgorithm).build();
        return verifier.verify(token).getSubject();
    }
}
