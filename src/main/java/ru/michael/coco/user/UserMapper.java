package ru.michael.coco.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.michael.coco.group.Group;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);

    default List<Long> mapGroupsToIds(List<Group> groups) {
        return groups.stream().map(Group::getId).collect(Collectors.toList());
    }

    @Mapping(source = "groups", target = "groupIds")
    UserDTO toDTOWithGroups(User user);

    @Mapping(target = "groups", ignore = true)
    User toEntityWithGroups(UserDTO userDTO);
}
