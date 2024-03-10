package ru.michael.coco.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.michael.coco.level_description.LevelDescriptionService;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.topic_description.TopicDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Configuration
public class DataLoaderConfig {
    private final UserService userService;
    private final TaskService taskService;
    private final TopicDescriptionService topicDescriptionService;
    private final LevelDescriptionService levelDescriptionService;
    private final TaskDescriptionService taskDescriptionService;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.xbank-dir}")
    private String xbankDir;
    @Value("${file.creds}")
    private String creds;

    @Autowired
    public DataLoaderConfig(UserService userService, TaskService taskService, TopicDescriptionService topicDescriptionService, LevelDescriptionService levelDescriptionService, TaskDescriptionService taskDescriptionService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.taskService = taskService;
        this.topicDescriptionService = topicDescriptionService;
        this.levelDescriptionService = levelDescriptionService;
        this.taskDescriptionService = taskDescriptionService;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public CommandLineRunner tasksLoader() {
        return args -> {
            topicDescriptionService.deleteAll();
            try (Stream<Path> stream = Files.walk(Path.of(xbankDir))) {
                stream.filter(Files::isDirectory)
                        .skip(1)
                        .forEach(p -> {
                            try {
                                taskDescriptionService.createTaskDescriptionByPath(p);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        };
    }

    @Bean
    public CommandLineRunner userLoader() {
        return args -> {
            userService.deleteAll();

            String line;
            String csvSplitBy = ",";
            try (BufferedReader br = new BufferedReader(new FileReader(creds))) {
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] userData = line.split(csvSplitBy);

                    String login = userData[0];
                    String password = userData[1];

                    User user = new User(login);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setRole(User.Role.ADMIN);
                    userService.save(user);
                }
            }
        };
    }

    @Bean
    @DependsOn({"tasksLoader", "userLoader"})
    public CommandLineRunner tasksInit() {
        return args -> {
            List<User> users = userService.findAll();
            List<TaskDescription> descriptions = taskDescriptionService.findAll();

            users.forEach(u -> descriptions.forEach(d -> {
                Task task = new Task(u, d, new ArrayList<>());
                taskService.save(task);
            }));
        };
    }
}
