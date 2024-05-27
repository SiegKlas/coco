package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.ScheduleDto;
import ru.michael.coco.entity.Schedule;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleDto toDto(Schedule schedule);

    Schedule toEntity(ScheduleDto scheduleDto);
}
