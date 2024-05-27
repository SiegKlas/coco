package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.Chat;
import ru.michael.coco.repository.ChatRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat createChat(Chat chat) {
        // Логика, если необходимо, перед сохранением чата
        return chatRepository.save(chat);
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }

    public Chat updateChat(Chat chat) {
        // Логика обновления чата, если необходимо
        return chatRepository.save(chat);
    }

    public void deleteChatById(Long id) {
        chatRepository.deleteById(id);
    }
}

