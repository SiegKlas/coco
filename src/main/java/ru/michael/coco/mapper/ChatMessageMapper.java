package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.ChatMessageDto;
import ru.michael.coco.entity.ChatMessage;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    ChatMessageDto toDto(ChatMessage chatMessage);

    ChatMessage toEntity(ChatMessageDto chatMessageDto);
}
