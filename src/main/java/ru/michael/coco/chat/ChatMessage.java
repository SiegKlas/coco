package ru.michael.coco.chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.user.User;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ChatMessage {
    @ManyToOne
    private User sender;
    @ManyToOne
    private Chat chat;
    private final String content;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
