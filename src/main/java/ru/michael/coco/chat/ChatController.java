package ru.michael.coco.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final TaskService taskService;
    private final TaskDescriptionService taskDescriptionService;
    private final ChatMessageMapper chatMessageMapper;

    @Autowired
    public ChatController(ChatService chatService, UserService userService, TaskService taskService, TaskDescriptionService taskDescriptionService, ChatMessageMapper chatMessageMapper) {
        this.chatService = chatService;
        this.userService = userService;
        this.taskService = taskService;
        this.taskDescriptionService = taskDescriptionService;
        this.chatMessageMapper = chatMessageMapper;
    }

    @GetMapping
    public String getChatPage(@RequestParam(name = "topicNumber") Integer topicNumber,
                              @RequestParam(name = "levelNumber") Integer levelNumber,
                              @RequestParam(name = "taskNumber") Integer taskNumber,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User student = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        TaskDescription taskDescription = taskDescriptionService
                .findTaskDescriptionByTopicNumberAndLevelNumberAndTaskNumber(
                        topicNumber, levelNumber, taskNumber
                ).orElseThrow();
        Task task = taskService.findTaskByUserAndTaskDescription(student, taskDescription).orElseThrow();
        Chat chat = chatService.getChatByTask(task);
        if (chat == null) {
            chat = chatService.createChat(student, task);
        }
        model.addAttribute("chatId", chat.getId());
        model.addAttribute("username", userDetails.getUsername());
        return "chat";
    }

    @GetMapping("/history/{chatId}")
    @ResponseBody
    public List<ChatMessageDto> history(@PathVariable Long chatId) {
        Chat chat = chatService.getChatById(chatId).orElseThrow();
        return chat.getMessages().stream()
                .map(chatMessageMapper::chatMessageToDto)
                .collect(Collectors.toList());
    }
}
