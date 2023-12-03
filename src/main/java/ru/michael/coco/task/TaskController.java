package ru.michael.coco.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.michael.coco.attempt.Attempt;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupDTO;
import ru.michael.coco.group.GroupMapper;
import ru.michael.coco.group.GroupService;
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
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @Autowired
    public TaskController(TaskDescriptionMapper taskDescriptionMapper, LevelDescriptionMapper levelDescriptionMapper, TopicDescriptionMapper topicDescriptionMapper, TaskDescriptionService taskDescriptionService, TaskService taskService, UserService userService,
                          TopicDescriptionService topicDescriptionService, LevelDescriptionService levelDescriptionService, GroupService groupService, GroupMapper groupMapper) {
        this.taskDescriptionMapper = taskDescriptionMapper;
        this.levelDescriptionMapper = levelDescriptionMapper;
        this.topicDescriptionMapper = topicDescriptionMapper;
        this.taskDescriptionService = taskDescriptionService;
        this.taskService = taskService;
        this.userService = userService;
        this.topicDescriptionService = topicDescriptionService;
        this.levelDescriptionService = levelDescriptionService;
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @GetMapping
    public String getTasks(@RequestParam(name = "group", required = false) String groupName,
                           @RequestParam(name = "topic", required = false) Integer topicNumber,
                           @RequestParam(name = "level", required = false) Integer levelNumber,
                           @RequestParam(name = "task", required = false) Integer taskNumber,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        if (groupName == null) {
            List<Group> groups = groupService.findAllGroups();
            model.addAttribute("groups", groups.stream().map(groupMapper::toDTO).toList());
        } else {
            Group group = groupService.findByName(groupName).orElseThrow();
            GroupDTO groupDTO = groupMapper.toDTO(group);
            model.addAttribute("selectedGroup", groupDTO);

            if (topicNumber == null && levelNumber == null && taskNumber == null) {
                List<TopicDescription> topicDescriptions = topicDescriptionService.findAllSorted();
                List<TopicDescriptionDTO> topicDescriptionDTOS = topicDescriptions.stream().map(topicDescriptionMapper::toDTO).toList();
                model.addAttribute("topicDescriptionDTOS", topicDescriptionDTOS);
            }
            if (topicNumber != null && levelNumber == null && taskNumber == null) {
                model.addAttribute("topicNumber", topicNumber);

                List<LevelDescription> levelDescriptions = levelDescriptionService.findAllByTopicDescriptionNumberSorted(topicNumber);
                List<LevelDescriptionDTO> levelDescriptionDTOS = levelDescriptions.stream().map(levelDescriptionMapper::toDTO).toList();
                model.addAttribute("levelDescriptionDTOS", levelDescriptionDTOS);
                List<Boolean> areLevelsLocked = levelDescriptionDTOS.stream()
                        .map(l -> false)
                        .toList();
                model.addAttribute("areLevelsLocked", areLevelsLocked);
            }
            if (topicNumber != null && levelNumber != null && taskNumber == null) {
                model.addAttribute("topicNumber", topicNumber);
                model.addAttribute("levelNumber", levelNumber);

                User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
                List<TaskDescription> taskDescriptions = taskDescriptionService.findTaskDescriptionsByTopicNumberAndLevelNumberSorted(topicNumber, levelNumber);
                List<TaskDescriptionDTO> taskDescriptionDTOS = taskDescriptions.stream().map(taskDescriptionMapper::toDTO).toList();
                List<Task.STATUS> statuses = taskDescriptions.stream().map(taskDescription -> taskService.getStatus(taskService.findTaskByUserAndTaskDescription(user, taskDescription).orElseThrow())).toList();
                model.addAttribute("taskDescriptionDTOS", taskDescriptionDTOS);
                model.addAttribute("statuses", statuses);
            }
            if (topicNumber != null && levelNumber != null && taskNumber != null) {
                model.addAttribute("topicNumber", topicNumber);
                model.addAttribute("levelNumber", levelNumber);
                model.addAttribute("taskNumber", taskNumber);

                TaskDescription taskDescription = taskDescriptionService.findTaskDescriptionByTopicNumberAndLevelNumberAndTaskNumber(topicNumber, levelNumber, taskNumber).orElseThrow();
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
        }
        return "tasks";
    }
}

