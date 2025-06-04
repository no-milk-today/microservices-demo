package ru.practicum.user.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.user.application.dto.UserRequestDto;
import ru.practicum.user.application.dto.UserResponseDto;
import ru.practicum.user.application.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDto dto);

    UserResponseDto toDto(User user);
}
