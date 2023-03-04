package com.itzroma.astrocornerapi.model.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterRequestDto(
        String email,
        String password,
        String firstName,
        String lastName,
        @NotNull
        String info,
        @NotNull
        String authProvider
) {
}
