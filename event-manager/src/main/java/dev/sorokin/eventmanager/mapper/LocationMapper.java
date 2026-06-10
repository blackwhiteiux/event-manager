package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.domain.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto toDto(Location location);
    Location toDomain(LocationDto location);
    LocationEntity toEntity(Location location);
    Location toDomain(LocationEntity location);
    LocationDto toDto(LocationEntity entity);
}
