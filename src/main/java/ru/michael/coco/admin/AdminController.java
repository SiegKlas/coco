package ru.michael.coco.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.michael.coco.chat.Chat;
import ru.michael.coco.chat.ChatService;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupService;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionMapper;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ChatService chatService;
    private final UserService userService;
    private final TaskDescriptionService taskDescriptionService;
    private final TaskService taskService;
    private final TaskDescriptionMapper taskDescriptionMapper;
    private final GroupService groupService;

    @Autowired
    public AdminController(ChatService chatService, UserService userService, TaskDescriptionService taskDescriptionService, TaskService taskService, TaskDescriptionMapper taskDescriptionMapper, GroupService groupService) {
        this.chatService = chatService;
        this.userService = userService;
        this.taskDescriptionService = taskDescriptionService;
        this.taskService = taskService;
        this.taskDescriptionMapper = taskDescriptionMapper;
        this.groupService = groupService;
    }

    @GetMapping
    public String getAdminPage(Model model) {
        List<Chat> chats = chatService.getAllChats();
        model.addAttribute("chats", chats);
        return "admin/main";
    }

    @GetMapping("/notifications")
    public String getNotificationsPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User admin = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Chat> chats = chatService.getAllChatsWithUnreadMessages(admin);
        model.addAttribute("chats", chats);
        return "admin/notifications";
    }

    @GetMapping("/tasks")
    public String getAdminTaskPage(@RequestParam("user") String username,
                                   @RequestParam("group") String groupName,
                                   @RequestParam("topic") Integer topicNumber,
                                   @RequestParam("level") Integer levelNumber,
                                   @RequestParam("task") Integer taskNumber,
                                   Model model) {
        User student = userService.findByUsername(username).orElseThrow();
        Group group = groupService.findByName(groupName).orElseThrow();
        TaskDescription taskDescription = taskDescriptionService
                .findTaskDescriptionByTopicNumberAndLevelNumberAndTaskNumber(topicNumber, levelNumber, taskNumber)
                .orElseThrow();
        Task task = taskService.findTaskByUserAndTaskDescription(student, taskDescription).orElseThrow();

        model.addAttribute("topicNumber", topicNumber);
        model.addAttribute("levelNumber", levelNumber);
        model.addAttribute("taskNumber", taskNumber);
        model.addAttribute("taskDescriptionDTO", taskDescriptionMapper.toDTO(taskDescription));
        model.addAttribute("dir_name", taskDescription.getFileName());

        model.addAttribute("attempts", task.getAttempts());
        model.addAttribute("status", taskService.getStatus(task));

        Chat chat = chatService.getChatByTask(task);
        if (chat == null) {
            chat = chatService.createChat(student, task);
        }
        model.addAttribute("chatId", chat.getId());

        return "admin/task";
    }


}

