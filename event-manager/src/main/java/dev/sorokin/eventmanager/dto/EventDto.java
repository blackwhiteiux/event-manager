package dev.sorokin.eventmanager.dto;


import dev.sorokin.eventmanager.domain.EventStatus;

import java.time.LocalDateTime;

public record EventDto(
        Long id,
        Long locationId,
        Long ownerId,
        String name,
        LocalDateTime startAt,
        Integer durationMinutes,
        Integer maxPlaces,
        Integer occupiedPlaces,
        Integer cost,
        EventStatus status

) {
}
