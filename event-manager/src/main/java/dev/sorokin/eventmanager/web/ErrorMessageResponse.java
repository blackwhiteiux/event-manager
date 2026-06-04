package dev.sorokin.eventmanager.web;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        LocalDateTime localDate
) {
}
