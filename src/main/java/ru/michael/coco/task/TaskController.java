package ru.michael.coco.task;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.michael.coco.attempt.Attempt;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskDescriptionService taskDescriptionService;
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskDescriptionService taskDescriptionService, TaskService taskService,
                          TaskRepository taskRepository, UserRepository userRepository) {
        this.taskDescriptionService = taskDescriptionService;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getTasks(@RequestParam Optional<Integer> topic,
                           @RequestParam Optional<Integer> level,
                           @RequestParam Optional<Integer> number,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        if (topic.isEmpty() && level.isEmpty() && number.isEmpty()) {
            model.addAttribute("topics", taskDescriptionService.getAllTopics());
            model.addAttribute("topicsNames", taskDescriptionService.getAllTopicsNames());
        }
        if (topic.isPresent() && level.isEmpty() && number.isEmpty()) {
            model.addAttribute("topic", topic.orElseThrow());
            List<Integer> levels = taskDescriptionService.getLevelsForTopic(topic.orElseThrow());
            model.addAttribute("levels", levels);
            List<Boolean> areLevelsLocked = levels.stream()
                    .map(l -> taskService.isLevelLocked(userDetails, topic.orElseThrow(), l))
                    .toList();
            model.addAttribute("areLevelsLocked", areLevelsLocked);
        }
        if (topic.isPresent() && level.isPresent() && number.isEmpty()) {
            model.addAttribute("topic", topic.orElseThrow());
            model.addAttribute("level", level.orElseThrow());
            model.addAttribute("numbers", taskDescriptionService.getNumbersForTopicAndLevel(topic.orElseThrow(),
                    level.orElseThrow()));
            List<TaskDescription> taskDescriptions = taskDescriptionService.getTaskDescriptions(topic.orElseThrow(),
                    level.orElseThrow());
            UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            List<Task> tasks = new ArrayList<>();
            for (TaskDescription taskDescription : taskDescriptions) {
                tasks.add(taskRepository.findTaskByUserAndTaskDescription(user, taskDescription));
            }
            model.addAttribute("tasks", tasks);
        }
        if (topic.isPresent() && level.isPresent() && number.isPresent()) {
            model.addAttribute("topic", topic.orElseThrow());
            model.addAttribute("level", level.orElseThrow());
            model.addAttribute("number", number.orElseThrow());
            TaskDescription taskDescription = taskDescriptionService.getTaskDescription(topic.orElseThrow(),
                    level.orElseThrow(), number.orElseThrow()).orElseThrow();
            model.addAttribute("task_description", taskDescription); // not necessary
            model.addAttribute("dir_name", taskDescription.getName());
            UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            Task task = taskRepository.findTaskByUserAndTaskDescription(user, taskDescription);
            List<Attempt> attempts = task.getAttempt();
            List<String> fileNames = attempts.stream()
                    .map(attempt -> Paths.get(attempt.getSolutionPath()).getFileName().toString())
                    .toList();
            model.addAttribute("attempts", attempts);
            model.addAttribute("file_names", fileNames);
            model.addAttribute("status", task.getStatus());
        }
        return "tasks";
    }
}
