package ru.michael.coco.file;

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
import ru.michael.coco.attempt.*;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskRepository;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionRepository;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class UploadController {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ResponseRepository responseRepository;
    private final TaskRepository taskRepository;
    private final TaskDescriptionRepository taskDescriptionRepository;
    private final AttemptRepository attemptRepository;
    @Value("${file.solutions}")
    private String solutionsDir;

    @Autowired
    public UploadController(UserRepository userRepository, FileRepository fileRepository,
                            ResponseRepository responseRepository, TaskRepository taskRepository,
                            TaskDescriptionRepository taskDescriptionRepository, AttemptRepository attemptRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.responseRepository = responseRepository;
        this.taskRepository = taskRepository;
        this.taskDescriptionRepository = taskDescriptionRepository;
        this.attemptRepository = attemptRepository;
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
            UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            FileEntity fileEntity = new FileEntity(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    fullPath,
                    user,
                    LocalDateTime.now()
            );
            fileRepository.save(fileEntity);
            final Map<String, String> json = new HashMap<>();
            json.put("fileName", file.getOriginalFilename());
            json.put("filePath", fullPath);
            json.put("dirName", dirName);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(otherServerUrl, json, String.class);
            Response response = JsonParser.parseJson(responseEntity.getBody());
            responseRepository.save(response);

            TaskDescription taskDescription =
                    taskDescriptionRepository.findTaskDescriptionByTopicAndLevelAndNumber(topic, level, number);
            Task task = taskRepository.findTaskByUserAndTaskDescription(user, taskDescription);

            Attempt attempt = new Attempt(user, task, fullPath, response);
            attemptRepository.save(attempt);
            task.getAttempt().add(attempt);

            if (response.getStatus().equals("success")) {
                task.setStatus(2);
            } else if (!task.getStatus().equals(2)) {
                task.setStatus(1);
            }

            System.out.println(responseEntity.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:" + request.getHeader("referer");
    }
}
