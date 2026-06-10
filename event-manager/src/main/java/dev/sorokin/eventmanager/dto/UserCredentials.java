package dev.sorokin.eventmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCredentials (
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
