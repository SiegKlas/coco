package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.GroupDto;
import ru.michael.coco.entity.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDto toDto(Group group);

    Group toEntity(GroupDto groupDto);
}
