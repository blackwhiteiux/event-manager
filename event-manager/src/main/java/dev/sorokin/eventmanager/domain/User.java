package dev.sorokin.eventmanager.domain;

public record User (
        Long id,
        String login,
        Integer age,
        UserRole role
) {
}
