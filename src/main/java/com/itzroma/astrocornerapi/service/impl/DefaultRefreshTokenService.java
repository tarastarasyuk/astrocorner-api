package com.itzroma.astrocornerapi.service.impl;

import com.itzroma.astrocornerapi.exception.EntityNotFoundException;
import com.itzroma.astrocornerapi.exception.ReAuthenticationRequiredException;
import com.itzroma.astrocornerapi.model.dto.AuthResponseDto;
import com.itzroma.astrocornerapi.model.dto.RefreshTokenRequestDto;
import com.itzroma.astrocornerapi.model.entity.RefreshToken;
import com.itzroma.astrocornerapi.repository.RefreshTokenRepository;
import com.itzroma.astrocornerapi.security.service.JwtService;
import com.itzroma.astrocornerapi.security.userdetails.DefaultUserDetails;
import com.itzroma.astrocornerapi.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRefreshTokenService implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> {
            throw new EntityNotFoundException("JWT refresh token not found, re-authenticate please");
        });
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshToken refreshToken = findByToken(refreshTokenRequestDto.refreshToken());
        if (jwtService.validateRefreshToken(refreshToken.getToken())) {
            String accessToken = jwtService.generateAccessToken(DefaultUserDetails.fromUser(refreshToken.getUser()));
            return new AuthResponseDto(accessToken, refreshToken.getToken());
        }
        throw new ReAuthenticationRequiredException();
    }

}
