package com.itzroma.astrocornerapi.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record SignInRequestDto(
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
                message = "Email is required and must be in the format user@example.com")
        String email,
        @NotEmpty(message = "Password is required.")
        String password) {
}
