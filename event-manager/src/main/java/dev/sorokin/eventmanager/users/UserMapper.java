package dev.sorokin.eventmanager.users;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(User user);
    User toDomain(UserEntity user);
    UserDto toDto(User user);
}
