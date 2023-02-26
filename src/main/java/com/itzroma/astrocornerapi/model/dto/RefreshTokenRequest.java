package com.itzroma.astrocornerapi.model.dto;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequest(
        @NotEmpty(message = "Refresh token is required.")
        String refreshToken) {
}
