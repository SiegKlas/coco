package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskRepository;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionRepository;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Controller
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

    @Autowired
    public AdminFileController(FileService fileService, TaskDescriptionService taskDescriptionService,
                               UserRepository userRepository, TaskRepository taskRepository,
                               TaskDescriptionRepository taskDescriptionRepository, TaskService taskService) {
        this.fileService = fileService;
        this.taskDescriptionService = taskDescriptionService;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskDescriptionRepository = taskDescriptionRepository;
        this.taskService = taskService;
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestPart("file") List<MultipartFile> files, Principal principal) throws IOException {
        Long userId = fileService.getUserIdFromPrincipal(principal);
        fileService.saveFiles(files, userId);
        return "redirect:/admin/files";
    }

    @GetMapping
    public String listAllFiles(Model model) {
        model.addAttribute("files", fileService.getAllFiles());
        return "adminFiles";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return "redirect:/admin/files";
    }

    @PostMapping("/bank")
    public String generateBank() throws IOException {
        String otherServerUrl = "http://127.0.0.1:5000/api/bank";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(otherServerUrl, new HashMap<>(),
                String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("xbank error");
        }

        taskDescriptionRepository.deleteAll();
        try (Stream<Path> stream = Files.walk(Path.of(xbankDir))) {
            stream.filter(Files::isDirectory)
                    .skip(1)
                    .forEach(taskDescriptionService::saveTask);
        }

        List<UserEntity> users = userRepository.findAll();
        List<TaskDescription> descriptions = taskDescriptionRepository.findAll();

        users.forEach(u -> descriptions.forEach(d -> {
            Task task = new Task(u, d, new ArrayList<>());
            taskService.save(task);
        }));

        return "redirect:/admin/files";
    }
}
