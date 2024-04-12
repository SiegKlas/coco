package ru.michael.coco.chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.task.Task;
import ru.michael.coco.user.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Chat {

    @OneToOne
    private final User student;
    @OneToOne
    private final Task task;
    @OneToMany(cascade = CascadeType.ALL)
    private final List<ChatMessage> messages = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
