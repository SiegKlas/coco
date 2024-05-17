package ru.michael.coco.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.michael.coco.group.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "groupIds", source = "groups", qualifiedByName = "mapGroupsToIds")
    UserDTO toDTO(User user);

    @Mapping(target = "groups", source = "groupIds", qualifiedByName = "mapIdsToGroups")
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);

    @Named("mapGroupsToIds")
    default List<Long> mapGroupsToIds(List<Group> groups) {
        return groups.stream().map(Group::getId).collect(Collectors.toList());
    }

    @Named("mapIdsToGroups")
    default List<Group> mapIdsToGroups(List<Long> ids) {
        return ids != null ? ids.stream().map(id -> {
            Group group = new Group();
            group.setId(id);
            return group;
        }).collect(Collectors.toList()) : new ArrayList<>();
    }
}
