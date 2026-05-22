package dev.sorokin.eventmanager.locations;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto toDto(Location location);
    Location toDomain(LocationDto location);
    LocationEntity toEntity(Location location);
    Location toDomain(LocationEntity location);
}
