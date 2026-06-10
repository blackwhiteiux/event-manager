package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.UserDto;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(User user);
    User toDomain(UserEntity user);
    UserDto toDto(User user);
}
