package com.itzroma.astrocornerapi.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.itzroma.astrocornerapi.security.userdetails.JwtUserDetails;
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

    @Value("${app.security.jwt.access.expiration-ms}")
    private Long accessTokenExpirationMs;

    @Value("${app.security.jwt.refresh.expiration-ms}")
    private Long refreshTokenExpirationMs;

    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public JwtService(@Value("${app.security.jwt.access.secret}") String accessSecret,
                      @Value("${app.security.jwt.refresh.secret}") String refreshSecret) {
        accessTokenAlgorithm = Algorithm.HMAC512(accessSecret);
        refreshTokenAlgorithm = Algorithm.HMAC512(refreshSecret);
    }

    public String generateAccessToken(JwtUserDetails jwtUserDetails) {
        return JWT.create()
                .withIssuer(request.getRequestURL().toString())
                .withSubject(jwtUserDetails.getUsername())
                .withClaim("roles", jwtUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .sign(accessTokenAlgorithm);
    }

    public String generateRefreshToken(JwtUserDetails jwtUserDetails) {
        return JWT.create()
                .withIssuer(request.getRequestURL().toString())
                .withSubject(jwtUserDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .sign(refreshTokenAlgorithm);
    }

    private boolean validateToken(String token, Algorithm algorithm) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (SignatureVerificationException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (TokenExpiredException ex) {
            log.error("JWT token is expired: {}", ex.getMessage());
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

    public String getSubjectFromAccessToken(String token) {
        JWTVerifier verifier = JWT.require(accessTokenAlgorithm).build();
        return verifier.verify(token).getSubject();
    }
}
