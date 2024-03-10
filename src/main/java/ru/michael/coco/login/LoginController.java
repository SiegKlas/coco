package ru.michael.coco.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.util.List;

@Controller
public class LoginController {
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public LoginController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Task> tasks = taskService.findTasksByUser(user);
        Integer solvedCount = Math.toIntExact(tasks.stream().map(taskService::getStatus).filter(e -> e.equals(Task.STATUS.SUCCESS)).count());
        Integer errorsCount = Math.toIntExact(tasks.stream().map(taskService::getStatus).filter(e -> e.equals(Task.STATUS.FAIL)).count());
        Integer unsolvedCount = Math.toIntExact(tasks.stream().map(taskService::getStatus).filter(e -> e.equals(Task.STATUS.UNSOLVED)).count());
        model.addAttribute("solved", solvedCount);
        model.addAttribute("errors", errorsCount);
        model.addAttribute("unsolved", unsolvedCount);
        return "home";
    }
}

