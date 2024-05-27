package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import ru.michael.coco.dto.UserDto;
import ru.michael.coco.entity.Group;
import ru.michael.coco.entity.GroupUser;
import ru.michael.coco.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "groupIds", source = "groupUsers", qualifiedByName = "groupUserToGroupIds")
    })
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Named("groupUserToGroupIds")
    default List<Long> groupUserToGroupIds(List<GroupUser> groupUsers) {
        return groupUsers.stream()
                .map(GroupUser::getGroup)
                .map(Group::getId)
                .collect(Collectors.toList());
    }
}
