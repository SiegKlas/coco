package ru.michael.coco.chat;

import lombok.Data;

@Data
public class ChatMessageDto {
    private String username;
    private String chatId;
    private String content;
}