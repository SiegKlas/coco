package ru.michael.coco.task;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionService;

import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskDescriptionService taskDescriptionService;
    private final TaskService taskService;

    public TaskController(TaskDescriptionService taskDescriptionService, TaskService taskService) {
        this.taskDescriptionService = taskDescriptionService;
        this.taskService = taskService;
    }

    @GetMapping
    public String getTasks(@RequestParam Optional<Integer> topic,
                           @RequestParam Optional<Integer> level,
                           @RequestParam Optional<Integer> number,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        if (topic.isEmpty() && level.isEmpty() && number.isEmpty()) {
            model.addAttribute("topics", taskDescriptionService.getAllTopics());
        }
        if (topic.isPresent() && level.isEmpty() && number.isEmpty()) {
            model.addAttribute("topic", topic.orElseThrow());
            model.addAttribute("levels", taskDescriptionService.getLevelsForTopic(topic.orElseThrow()));
        }
        if (topic.isPresent() && level.isPresent() && number.isEmpty()) {
            model.addAttribute("topic", topic.orElseThrow());
            model.addAttribute("level", level.orElseThrow());
            model.addAttribute("numbers", taskDescriptionService.getNumbersForTopicAndLevel(topic.orElseThrow(),
                    level.orElseThrow()));
        }
        if (topic.isPresent() && level.isPresent() && number.isPresent()) {
            model.addAttribute("topic", topic.orElseThrow());
            model.addAttribute("level", level.orElseThrow());
            model.addAttribute("number", number.orElseThrow());
            TaskDescription taskDescription = taskDescriptionService.getTaskDescription(topic.orElseThrow(),
                    level.orElseThrow(), number.orElseThrow()).orElseThrow();
            model.addAttribute("task_description", taskDescription);
            model.addAttribute("dir_name", taskDescription.getName());
        }
        return "tasks";
    }
}
