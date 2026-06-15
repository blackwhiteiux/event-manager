package dev.sorokin.eventmanager.dto;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public record EventCreateRequestDto(
        @NotBlank
        String name,

        @NotNull
        @Positive
        Integer maxPlaces,

        @NotNull
        @Future
        OffsetDateTime date,

        @NotNull
        @Positive
        Integer cost,

        @NotNull
        @Min(30)
        Integer duration,

        @NotNull
        @Positive
        Long locationId
) {
}
