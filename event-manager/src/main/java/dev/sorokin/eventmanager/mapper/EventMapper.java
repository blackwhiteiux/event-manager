package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "ownerId", source = "owner.id")
    EventDto toDto(EventEntity event);
}
