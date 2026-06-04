package dev.sorokin.eventmanager.locations;

import jakarta.validation.constraints.*;


public record LocationDto (
        @Null
        Long id,

        @NotBlank
        String name,

        @NotBlank
        @NotNull
        String address,

        @Min(5)
        Integer capacity,

        String description
) {
}
