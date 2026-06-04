package dev.sorokin.eventmanager.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import static dev.sorokin.eventmanager.users.UserConstraints.MINIMAL_USER_AGE;

public record UserRegistration (
        @NotBlank
        String login,

        @NotBlank
        String password,

        @Min(18)
        Integer age
) {
}
