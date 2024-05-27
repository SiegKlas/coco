package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.JournalDto;
import ru.michael.coco.entity.Journal;

@Mapper(componentModel = "spring")
public interface JournalMapper {

    JournalDto toDto(Journal journal);

    Journal toEntity(JournalDto journalDto);
}
