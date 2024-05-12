package ru.michael.coco.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.michael.coco.user.UserService;

import java.util.Objects;

@Controller
public class WebSocketChatController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ChatMessageMapper chatMessageMapper;
    private final UserService userService;

    @Autowired
    public WebSocketChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService, ChatMessageMapper chatMessageMapper, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.chatMessageMapper = chatMessageMapper;
        this.userService = userService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDto messageDto) {
        logger.info("Received message DTO: {}", messageDto);

        // Convert DTO to Entity using the mapper and passing services as @Context
        ChatMessage chatMessage = chatMessageMapper.dtoToChatMessage(messageDto, userService, chatService);

        // Save the chat message
        chatService.saveChatMessage(chatMessage);

        // Convert Entity back to DTO to send back to users
        ChatMessageDto dtoToSend = chatMessageMapper.chatMessageToDto(chatMessage);
        messagingTemplate.convertAndSend("/topic/publicChatRoom/" + messageDto.getChatId(), dtoToSend);
    }

    @MessageMapping("/addUser")
    public void addUser(@Payload ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor headerAccessor) {
        String username = chatMessageDto.getUsername();
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", username);
        messagingTemplate.convertAndSend("/topic/publicChatRoom/" + chatMessageDto.getChatId(), chatMessageDto);
    }
}
