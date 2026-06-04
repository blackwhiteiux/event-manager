package dev.sorokin.eventmanager.users;

import jakarta.validation.constraints.NotBlank;

public record UserCredentials (
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
