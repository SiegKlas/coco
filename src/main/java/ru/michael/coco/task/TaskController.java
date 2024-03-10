package ru.michael.coco.task;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.michael.coco.attempt.Attempt;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.level_description.LevelDescriptionDTO;
import ru.michael.coco.level_description.LevelDescriptionMapper;
import ru.michael.coco.level_description.LevelDescriptionService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionDTO;
import ru.michael.coco.task_description.TaskDescriptionMapper;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.topic_description.TopicDescriptionDTO;
import ru.michael.coco.topic_description.TopicDescriptionMapper;
import ru.michael.coco.topic_description.TopicDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskDescriptionMapper taskDescriptionMapper;
    private final LevelDescriptionMapper levelDescriptionMapper;
    private final TopicDescriptionMapper topicDescriptionMapper;
    private final TaskDescriptionService taskDescriptionService;
    private final TaskService taskService;
    private final UserService userService;
    private final TopicDescriptionService topicDescriptionService;
    private final LevelDescriptionService levelDescriptionService;

    @Autowired
    public TaskController(TaskDescriptionService taskDescriptionService, TaskService taskService, UserService userService,
                          TopicDescriptionService topicDescriptionService, LevelDescriptionService levelDescriptionService) {
        this.taskDescriptionMapper = Mappers.getMapper(TaskDescriptionMapper.class);
        this.levelDescriptionMapper = Mappers.getMapper(LevelDescriptionMapper.class);
        this.topicDescriptionMapper = Mappers.getMapper(TopicDescriptionMapper.class);
        this.taskDescriptionService = taskDescriptionService;
        this.taskService = taskService;
        this.userService = userService;
        this.topicDescriptionService = topicDescriptionService;
        this.levelDescriptionService = levelDescriptionService;
    }

    @GetMapping
    public String getTasks(@RequestParam Optional<Integer> topicNumber,
                           @RequestParam Optional<Integer> levelNumber,
                           @RequestParam Optional<Integer> taskNumber,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        if (topicNumber.isEmpty() && levelNumber.isEmpty() && taskNumber.isEmpty()) {
            List<TopicDescription> topicDescriptions = topicDescriptionService.findAllSorted();
            List<TopicDescriptionDTO> topicDescriptionDTOS =
                    topicDescriptions.stream().map(topicDescriptionMapper::toDTO).toList();
            model.addAttribute("topicDescriptionDTOS", topicDescriptionDTOS);
        }
        if (topicNumber.isPresent() && levelNumber.isEmpty() && taskNumber.isEmpty()) {
            model.addAttribute("topicNumber", topicNumber.orElseThrow());

            List<LevelDescription> levelDescriptions =
                    levelDescriptionService.findAllByTopicDescriptionNumberSorted(topicNumber.orElseThrow());
            List<LevelDescriptionDTO> levelDescriptionDTOS =
                    levelDescriptions.stream().map(levelDescriptionMapper::toDTO).toList();
            model.addAttribute("levelDescriptionDTOS", levelDescriptionDTOS);
            List<Boolean> areLevelsLocked = levelDescriptionDTOS.stream()
                    .map(l -> false)
                    .toList();
            model.addAttribute("areLevelsLocked", areLevelsLocked);
        }
        if (topicNumber.isPresent() && levelNumber.isPresent() && taskNumber.isEmpty()) {
            model.addAttribute("topicNumber", topicNumber.orElseThrow());
            model.addAttribute("levelNumber", levelNumber.orElseThrow());

            User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
            List<TaskDescription> taskDescriptions =
                    taskDescriptionService.findTaskDescriptionsByTopicNumberAndLevelNumberSorted(
                            topicNumber.orElseThrow(), levelNumber.orElseThrow()
                    );
            List<TaskDescriptionDTO> taskDescriptionDTOS =
                    taskDescriptions.stream().map(taskDescriptionMapper::toDTO).toList();
            List<Task.STATUS> statuses = taskDescriptions.stream().map(
                    taskDescription -> taskService.getStatus(
                            taskService.findTaskByUserAndTaskDescription(user, taskDescription).orElseThrow()
                    )
            ).toList();
            model.addAttribute("taskDescriptionDTOS", taskDescriptionDTOS);
            model.addAttribute("statuses", statuses);
        }
        if (topicNumber.isPresent() && levelNumber.isPresent() && taskNumber.isPresent()) {
            model.addAttribute("topicNumber", topicNumber.orElseThrow());
            model.addAttribute("levelNumber", levelNumber.orElseThrow());
            model.addAttribute("taskNumber", taskNumber.orElseThrow());

            TaskDescription taskDescription = taskDescriptionService.findTaskDescriptionByTopicNumberAndLevelNumberAndTaskNumber(
                    topicNumber.orElseThrow(),
                    levelNumber.orElseThrow(),
                    taskNumber.orElseThrow()
            ).orElseThrow();
            TaskDescriptionDTO taskDescriptionDTO = taskDescriptionMapper.toDTO(taskDescription);
            model.addAttribute("taskDescriptionDTO", taskDescriptionDTO);

            model.addAttribute("dir_name", taskDescription.getFileName());
            User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
            Task task = taskService.findTaskByUserAndTaskDescription(user, taskDescription).orElseThrow();
            List<Attempt> attempts = task.getAttempts();
            List<String> fileNames = attempts.stream()
                    .map(attempt -> Paths.get(attempt.getSolutionPath()).getFileName().toString())
                    .toList();
            model.addAttribute("attempts", attempts);
            model.addAttribute("file_names", fileNames);
            model.addAttribute("status", taskService.getStatus(task));
        }
        return "tasks";
    }
}
