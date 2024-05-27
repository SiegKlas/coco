package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import ru.michael.coco.dto.GroupDto;
import ru.michael.coco.entity.Group;
import ru.michael.coco.entity.GroupUser;
import ru.michael.coco.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mappings({
            @Mapping(target = "teacherId", source = "groupUsers", qualifiedByName = "groupUserToTeacherId"),
            @Mapping(target = "studentIds", source = "groupUsers", qualifiedByName = "groupUserToStudentIds")
    })
    GroupDto toDto(Group group);

    Group toEntity(GroupDto groupDto);

    @Named("groupUserToTeacherId")
    default Long groupUserToTeacherId(List<GroupUser> groupUsers) {
        return groupUsers.stream()
                .filter(gu -> gu.getUser().getRole() == User.Role.TEACHER)
                .map(gu -> gu.getUser().getId())
                .findFirst()
                .orElse(null);
    }

    @Named("groupUserToStudentIds")
    default List<Long> groupUserToStudentIds(List<GroupUser> groupUsers) {
        return groupUsers.stream()
                .filter(gu -> gu.getUser().getRole() == User.Role.STUDENT)
                .map(gu -> gu.getUser().getId())
                .collect(Collectors.toList());
    }
}
