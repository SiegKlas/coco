package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.michael.coco.admin.statistics.CsvWriter;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskRepository;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionRepository;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/admin/files")
public class AdminFileController {
    private final FileService fileService;
    private final TaskDescriptionService taskDescriptionService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskDescriptionRepository taskDescriptionRepository;
    @Value("${file.xbank-dir}")
    private String xbankDir;
    private final TaskService taskService;
    private final CsvWriter csvWriter;

    @Autowired
    public AdminFileController(FileService fileService, TaskDescriptionService taskDescriptionService, UserRepository userRepository, TaskRepository taskRepository, TaskDescriptionRepository taskDescriptionRepository, TaskService taskService, CsvWriter csvWriter) {
        this.fileService = fileService;
        this.taskDescriptionService = taskDescriptionService;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskDescriptionRepository = taskDescriptionRepository;
        this.taskService = taskService;
        this.csvWriter = csvWriter;
    }

    @PostMapping("/bank")
    public String generateBank(@RequestParam Long groupId, @RequestParam Long bankId) throws IOException {
        String otherServerUrl = "http://127.0.0.1:5000/api/bank";
        Map<String, Long> requestParams = Map.of("groupId", groupId, "bankId", bankId);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(otherServerUrl, requestParams, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("xbank error");
        }

        taskDescriptionRepository.deleteAll();
        try (Stream<Path> stream = Files.walk(Path.of(xbankDir))) {
            stream.filter(Files::isDirectory).skip(1).forEach(path -> {
                try {
                    taskDescriptionService.createTaskDescriptionByPath(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        List<User> users = userRepository.findAll();
        List<TaskDescription> descriptions = taskDescriptionRepository.findAll();

        users.forEach(u -> descriptions.forEach(d -> {
            Task task = new Task(u, d, new ArrayList<>());
            taskService.save(task);
        }));

        return "redirect:/admin/files";
    }
}
