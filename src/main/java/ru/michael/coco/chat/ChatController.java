package ru.michael.coco.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final TaskService taskService;
    private final TaskDescriptionService taskDescriptionService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService, TaskService taskService, TaskDescriptionService taskDescriptionService) {
        this.chatService = chatService;
        this.userService = userService;
        this.taskService = taskService;
        this.taskDescriptionService = taskDescriptionService;
    }

    @GetMapping
    public String getChatPage(@RequestParam(name = "topicNumber") Integer topicNumber,
                              @RequestParam(name = "levelNumber") Integer levelNumber,
                              @RequestParam(name = "taskNumber") Integer taskNumber,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User student = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Chat chat = chatService.getChatByStudent(student);
        if (chat == null) {
            TaskDescription taskDescription = taskDescriptionService
                    .findTaskDescriptionByTopicNumberAndLevelNumberAndTaskNumber(
                            topicNumber, levelNumber, taskNumber
                    ).orElseThrow();
            Task task = taskService.findTaskByUserAndTaskDescription(student, taskDescription).orElseThrow();
            chat = chatService.createChat(student, task);
        }
        model.addAttribute("chatId", chat.getId());
        return "chat";
    }

    @PostMapping("/sendMessage")
    @ResponseBody
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDto messageDto,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        // Логика для отправки сообщения в чат
        return ResponseEntity.ok().build();
    }
}

