package com.itzroma.astrocornerapi.service;

import com.itzroma.astrocornerapi.model.dto.AuthResponseDto;
import com.itzroma.astrocornerapi.model.dto.RefreshTokenRequestDto;
import com.itzroma.astrocornerapi.model.entity.RefreshToken;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken findByToken(String token);

    @Transactional
    void deleteByToken(String token);

}
