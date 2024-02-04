package ru.michael.coco.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskRepository;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionRepository;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Configuration
public class DataLoaderConfig {

    private final TaskDescriptionService taskDescriptionService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskRepository taskRepository;
    private final TaskDescriptionRepository taskDescriptionRepository;
    @Value("${file.xbank-dir}")
    private String xbankDir;
    @Value("${file.creds}")
    private String creds;
    private final TaskService taskService;

    @Autowired
    public DataLoaderConfig(TaskDescriptionService taskDescriptionService, UserRepository userRepository,
                            PasswordEncoder passwordEncoder, TaskRepository taskRepository,
                            TaskDescriptionRepository taskDescriptionRepository, TaskService taskService) {
        this.taskDescriptionService = taskDescriptionService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskRepository = taskRepository;
        this.taskDescriptionRepository = taskDescriptionRepository;
        this.taskService = taskService;
    }

    @Bean
    public CommandLineRunner tasksLoader() {
        return args -> {
            taskDescriptionRepository.deleteAll();
            try (Stream<Path> stream = Files.walk(Path.of(xbankDir))) {
                stream.filter(Files::isDirectory)
                        .skip(1)
                        .forEach(taskDescriptionService::saveTask);
            }
        };
    }

    @Bean
    public CommandLineRunner userLoader() {
        return args -> {
            userRepository.deleteAll();

            String line;
            String csvSplitBy = ",";
            try (BufferedReader br = new BufferedReader(new FileReader(creds))) {
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] userData = line.split(csvSplitBy);

                    String login = userData[0];
                    String password = userData[1];

                    User user = new User();
                    user.setUsername(login);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setRole(User.Role.ADMIN);
                    userRepository.save(user);
                }
            }
        };
    }

    @Bean
    @DependsOn({"tasksLoader", "userLoader"})
    public CommandLineRunner tasksInit() {
        return args -> {
            List<User> users = userRepository.findAll();
            List<TaskDescription> descriptions = taskDescriptionRepository.findAll();

            users.forEach(u -> descriptions.forEach(d -> {
                Task task = new Task(u, d, new ArrayList<>());
                if (task.getTaskDescription().getLevel().equals(1)) {
                    task.setIsLocked(false);
                }
                taskService.save(task);
            }));
        };
    }
}
