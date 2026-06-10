package dev.sorokin.eventmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;


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
