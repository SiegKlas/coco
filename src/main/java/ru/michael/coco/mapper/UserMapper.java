package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.UserDto;
import ru.michael.coco.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
