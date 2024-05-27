package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Chatters {
    @EmbeddedId
    private ChattersId id;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}
