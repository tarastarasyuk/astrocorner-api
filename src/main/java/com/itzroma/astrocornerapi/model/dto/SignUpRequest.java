package com.itzroma.astrocornerapi.model.dto;

import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
                message = "Email is required and must be in the format user@example.com")
        String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,}$",
                message = "Password is required and must be at least 8 characters long, containing at least one uppercase letter, one lowercase letter, and one digit.")
        String password) {
}