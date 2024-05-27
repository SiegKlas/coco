package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.ChatMessage;
import ru.michael.coco.repository.ChatMessageRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage createChatMessage(ChatMessage chatMessage) {
        // Логика, если необходимо, перед сохранением сообщения чата
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getAllChatMessages() {
        return chatMessageRepository.findAll();
    }

    public Optional<ChatMessage> getChatMessageById(Long id) {
        return chatMessageRepository.findById(id);
    }

    public ChatMessage updateChatMessage(ChatMessage chatMessage) {
        // Логика обновления сообщения чата, если необходимо
        return chatMessageRepository.save(chatMessage);
    }

    public void deleteChatMessageById(Long id) {
        chatMessageRepository.deleteById(id);
    }
}
