package ru.michael.coco.chat;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

@Mapper(componentModel = "spring", uses = {UserService.class, ChatService.class})
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    @Mapping(target = "username", source = "sender.username")
    @Mapping(target = "chatId", expression = "java(chatMessage.getChat() != null ? String.valueOf(chatMessage.getChat().getId()) : null)")
    ChatMessageDto chatMessageToDto(ChatMessage chatMessage);

    @Mapping(target = "content", source = "content")
    ChatMessage dtoToChatMessage(ChatMessageDto chatMessageDto, @Context UserService userService, @Context ChatService chatService);

    @AfterMapping
    default void linkEntities(ChatMessageDto chatMessageDto, @MappingTarget ChatMessage chatMessage, @Context UserService userService, @Context ChatService chatService) {
        if (chatMessageDto.getUsername() != null) {
            User user = userService.findByUsername(chatMessageDto.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + chatMessageDto.getUsername()));
            chatMessage.setSender(user);
        }
        if (chatMessageDto.getChatId() != null) {
            Chat chat = chatService.getChatById(Long.parseLong(chatMessageDto.getChatId()))
                    .orElseThrow(() -> new RuntimeException("Chat not found: " + chatMessageDto.getChatId()));
            chatMessage.setChat(chat);
        }
    }
}