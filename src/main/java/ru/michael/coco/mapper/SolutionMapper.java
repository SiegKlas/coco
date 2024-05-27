package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.SolutionDto;
import ru.michael.coco.entity.Solution;

@Mapper(componentModel = "spring")
public interface SolutionMapper {

    SolutionDto toDto(Solution solution);

    Solution toEntity(SolutionDto solutionDto);
}
