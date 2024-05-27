package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.ChatDto;
import ru.michael.coco.entity.Chat;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    ChatDto toDto(Chat chat);

    Chat toEntity(ChatDto chatDto);
}
