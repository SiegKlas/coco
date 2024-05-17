package ru.michael.coco.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.michael.coco.group.GroupService;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.task_description.TaskDescriptionService;
import ru.michael.coco.topic_description.TopicDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Configuration
public class DataLoaderConfig {
    private final UserService userService;
    private final TaskService taskService;
    private final TopicDescriptionService topicDescriptionService;
    private final TaskDescriptionService taskDescriptionService;
    private final PasswordEncoder passwordEncoder;
    private final GroupService groupService;
    private final GroupAssignmentService groupAssignmentService;

    @Value("${file.xbank-dir}")
    private String xbankDir;
    @Value("${file.creds}")
    private String creds;

    @Autowired
    public DataLoaderConfig(UserService userService, TaskService taskService, TopicDescriptionService topicDescriptionService, TaskDescriptionService taskDescriptionService, PasswordEncoder passwordEncoder, GroupService groupService, GroupAssignmentService groupAssignmentService) {
        this.userService = userService;
        this.taskService = taskService;
        this.topicDescriptionService = topicDescriptionService;
        this.taskDescriptionService = taskDescriptionService;
        this.passwordEncoder = passwordEncoder;
        this.groupService = groupService;
        this.groupAssignmentService = groupAssignmentService;
    }


    //    @Bean
//    public CommandLineRunner tasksLoader() {
//        return args -> {
//            topicDescriptionService.deleteAll();
//            try (Stream<Path> stream = Files.walk(Path.of(xbankDir))) {
//                stream.filter(Files::isDirectory)
//                        .skip(1)
//                        .forEach(p -> {
//                            try {
//                                taskDescriptionService.createTaskDescriptionByPath(p);
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        });
//            }
//        };
//    }
//
    @Bean
    public CommandLineRunner userLoader() {
        return args -> {
            userService.deleteAll();

            String line;
            String csvSplitBy = ",";
            try (BufferedReader br = new BufferedReader(new FileReader(creds))) {
                br.readLine(); // Пропуск заголовка
                while ((line = br.readLine()) != null) {
                    String[] userData = line.split(csvSplitBy);

                    String login = userData[0];
                    String password = userData[1];
                    String role = userData[2];
                    String groupName = userData[3];

                    User user = new User();
                    user.setUsername(login);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setRole(User.Role.valueOf(role));
                    user.setGroups(new ArrayList<>());

                    userService.save(user); // Сохранение пользователя до добавления группы

                    if (!groupName.equals("null")) {
                        groupAssignmentService.addUserToGroup(user, groupName);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
//
//    @Bean
//    @DependsOn({"tasksLoader", "userLoader"})
//    public CommandLineRunner tasksInit() {
//        return args -> {
//            List<User> users = userService.findAll();
//            List<TaskDescription> descriptions = taskDescriptionService.findAll();
//
//            users.forEach(u -> descriptions.forEach(d -> {
//                Task task = new Task(u, d, new ArrayList<>());
//                taskService.save(task);
//            }));
//        };
//    }
}
