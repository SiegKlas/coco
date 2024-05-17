package ru.michael.coco.group;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.michael.coco.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "studentIds", source = "students", qualifiedByName = "mapStudentsToIds")
    GroupDTO toDTO(Group group);

    @Mapping(target = "teacher", source = "teacherId", qualifiedByName = "mapIdToTeacher")
    @Mapping(target = "students", source = "studentIds", qualifiedByName = "mapIdsToStudents")
    Group toEntity(GroupDTO groupDTO);

    @Named("mapStudentsToIds")
    default List<Long> mapStudentsToIds(List<User> students) {
        return students.stream().map(User::getId).collect(Collectors.toList());
    }

    @Named("mapIdsToStudents")
    default List<User> mapIdsToStudents(List<Long> ids) {
        return ids.stream().map(id -> {
            User user = new User();
            user.setId(id);
            return user;
        }).collect(Collectors.toList());
    }

    @Named("mapIdToTeacher")
    default User mapIdToTeacher(Long id) {
        User teacher = new User();
        teacher.setId(id);
        return teacher;
    }
}
