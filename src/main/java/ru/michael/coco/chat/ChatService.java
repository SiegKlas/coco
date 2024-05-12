package ru.michael.coco.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.task.Task;
import ru.michael.coco.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public Chat createChat(User student, Task task) {
        var chatters = new ArrayList<User>();
        if (student.getTeacher() != null) {
            chatters.add(student.getTeacher());
        }
        chatters.add(student);
        Chat chat = new Chat(chatters, task);
        return chatRepository.save(chat);
    }

    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public void saveChat(Chat chat) {
        chatRepository.save(chat);
    }

    public Chat getChatByTask(Task task) {
        return chatRepository.findByTask(task);
    }

    @Transactional
    public void saveChatMessage(ChatMessage chatMessage) {
        // Ensure we have the chat loaded
        Chat chat = chatRepository.findById(chatMessage.getChat().getId())
                .orElseThrow(() -> new RuntimeException("Chat not found: " + chatMessage.getChat().getId()));

        // Add the message to the chat
        chat.getMessages().add(chatMessage);

        // Save both the message and the chat
        chatMessageRepository.save(chatMessage);
        chatRepository.save(chat);
    }
}