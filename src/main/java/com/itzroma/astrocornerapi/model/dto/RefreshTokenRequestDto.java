package com.itzroma.astrocornerapi.model.dto;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequestDto(
        @NotEmpty(message = "Refresh token is required.")
        String refreshToken) {
}
