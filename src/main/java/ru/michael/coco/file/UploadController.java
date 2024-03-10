package ru.michael.coco.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.michael.coco.attempt.Attempt;
import ru.michael.coco.attempt.Response;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class UploadController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskDescriptionService taskDescriptionService;
    @Value("${file.solutions}")
    private String solutionsDir;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public UploadController(TaskService taskService, UserService userService, TaskDescriptionService taskDescriptionService) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskDescriptionService = taskDescriptionService;
    }

    @PostMapping("/solution")
    public String handleSolutionUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam("dir_name") String dirName,
                                       @RequestParam("topic") Integer topic,
                                       @RequestParam("level") Integer level,
                                       @RequestParam("number") Integer number) {
        try {
            String fullPath = FileManager.moveMultipartFileToSubfolder(Path.of(solutionsDir),
                    userDetails.getUsername(), file);

            String otherServerUrl = "http://127.0.0.1:5000/api/check";
            RestTemplate restTemplate = new RestTemplate();
            User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

            final Map<String, String> json = new HashMap<>();
            json.put("fileName", file.getOriginalFilename());
            json.put("filePath", fullPath);
            json.put("dirName", dirName);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(otherServerUrl, json, String.class);
            Response response = objectMapper.readValue(responseEntity.getBody(), Response.class);

            TaskDescription taskDescription = taskDescriptionService.findTaskDescriptionByTopicNumberAndLevelNumberAndTaskNumber(
                    topic, level, number
            ).orElseThrow();
            Task task = taskService.findTaskByUserAndTaskDescription(user, taskDescription).orElseThrow();

            Attempt attempt = new Attempt(fullPath, task, response);
            task.getAttempts().add(attempt);

            taskService.save(task);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:" + request.getHeader("referer");
    }
}
