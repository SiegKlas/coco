package ru.michael.coco.login;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskRepository;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.util.List;

@Controller
public class LoginController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public LoginController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Task> tasks = taskRepository.findTasksByUser(user);
        Integer solvedCount = Math.toIntExact(tasks.stream().map(Task::getStatus).filter(e -> e.equals(2)).count());
        Integer errorsCount = Math.toIntExact(tasks.stream().map(Task::getStatus).filter(e -> e.equals(1)).count());
        Integer unsolvedCount = Math.toIntExact(tasks.stream().map(Task::getStatus).filter(e -> e.equals(0)).count());
        model.addAttribute("solved", solvedCount);
        model.addAttribute("errors", errorsCount);
        model.addAttribute("unsolved", unsolvedCount);
        return "home";
    }
}

