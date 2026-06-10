package dev.sorokin.eventmanager.dto;

import dev.sorokin.eventmanager.domain.UserRole;

public record UserDto (
        Long id,
        String login,
        Integer age,
        UserRole role
) {
}
