package ru.michael.coco.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.michael.coco.email.EmailService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.user.UserService;

import java.util.Objects;

@Controller
public class WebSocketChatController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ChatMessageMapper chatMessageMapper;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public WebSocketChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService, ChatMessageMapper chatMessageMapper, UserService userService, EmailService emailService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.chatMessageMapper = chatMessageMapper;
        this.userService = userService;
        this.emailService = emailService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDto messageDto) {
        ChatMessage chatMessage = chatMessageMapper.dtoToChatMessage(messageDto, userService, chatService);
        chatService.saveChatMessage(chatMessage);
        ChatMessageDto dtoToSend = chatMessageMapper.chatMessageToDto(chatMessage);
        messagingTemplate.convertAndSend("/topic/publicChatRoom/" + messageDto.getChatId(), dtoToSend);

        if (!"admin".equals(messageDto.getUsername())) {
            String adminEmail = "thaspringoflife@gmail.com";
            TaskDescription taskDescription = chatMessage.getChat().getTask().getTaskDescription();
            String link = "http://localhost:8080/admin/tasks?user=" + messageDto.getUsername() +
                    "&topic=" + taskDescription.getLevelDescription().getTopicDescription().getNumber() +
                    "&level=" + taskDescription.getLevelDescription().getNumber() +
                    "&task=" + taskDescription.getNumber();
            String mailContent = "New message from " + messageDto.getUsername() + ". Click here to view the chat: " + link;
            emailService.sendSimpleMessage(adminEmail, "New Message in Chat", mailContent);
        }
    }

    @MessageMapping("/addUser")
    public void addUser(@Payload ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor headerAccessor) {
        String username = chatMessageDto.getUsername();
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", username);
        messagingTemplate.convertAndSend("/topic/publicChatRoom/" + chatMessageDto.getChatId(), chatMessageDto);
    }
}
