package ru.michael.coco.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.task.Task;
import ru.michael.coco.user.User;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat createChat(User student, Task task) {
        Chat chat = new Chat(student, task);
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

    public Chat getChatByStudent(User student) {
        return chatRepository.findByStudent(student);
    }
}