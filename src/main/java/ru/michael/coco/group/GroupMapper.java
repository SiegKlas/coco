package ru.michael.coco.group;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupDTO toDTO(Group group);

    Group toEntity(GroupDTO groupDTO);
}
