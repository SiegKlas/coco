package ru.michael.coco.dto;


import lombok.Data;

@Data
public class ChatMessageDto {
    private Long id;
    private Long senderId;
    private String content;
}
