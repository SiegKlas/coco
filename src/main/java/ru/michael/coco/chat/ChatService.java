package ru.michael.coco.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.bank.Bank;
import ru.michael.coco.bank.BankRepository;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupRepository;
import ru.michael.coco.task.Task;
import ru.michael.coco.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GroupRepository groupRepository;
    private final BankRepository bankRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, ChatMessageRepository chatMessageRepository, GroupRepository groupRepository, BankRepository bankRepository) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.groupRepository = groupRepository;
        this.bankRepository = bankRepository;
    }

    public Chat createChat(User student, Task task) {
        Group group = student.getGroups().stream().findFirst().orElseThrow(() -> new RuntimeException("User is not in a group"));
        Bank activeBank = group.getBanks().stream().findFirst().orElseThrow(() -> new RuntimeException("Group has no active bank"));

        var chatters = new ArrayList<User>();
        chatters.add(activeBank.getGroup().getTeacher());
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

    public List<Chat> getAllChatsWithUnreadMessages(User user) {
        var chats = getAllChats();
        return chats.stream().filter(chat -> chat.getChatters().contains(user)).filter(chat -> !chat.getMessages().isEmpty()).toList();
    }
}
