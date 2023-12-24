package ru.michael.coco.task;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskRepository taskRepository, TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String tasksList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Task> tasks = taskService.findTasksByUser(userDetails);
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @GetMapping("/{id}")
    public String task() {
        return "task";
    }
}
