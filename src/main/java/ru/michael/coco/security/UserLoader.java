package ru.michael.coco.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.Group;
import ru.michael.coco.entity.User;
import ru.michael.coco.service.GroupService;
import ru.michael.coco.service.UserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Component
public class UserLoader implements CommandLineRunner {

    private final UserService userService;
    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;
    @Value("${file.creds}")
    private String credsFilePath;

    @Autowired
    public UserLoader(UserService userService, GroupService groupService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.groupService = groupService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        loadUsersFromCsv(credsFilePath);
    }

    private void loadUsersFromCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Пропускаем заголовок
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 5) {
                    continue;
                }

                String login = values[0];
                String password = values[1];
                User.Role role = User.Role.valueOf(values[2]);
                String groupName = values[3].trim().isEmpty() || values[3].equalsIgnoreCase("null") ? null : values[3].trim();
                String email = values[4].trim().isEmpty() || values[4].equalsIgnoreCase("null") ? null : values[4].trim();

                Optional<User> existingUser = userService.getUserByUsername(login);
                if (existingUser.isPresent()) {
                    continue;
                }

                User user = new User();
                user.setUsername(login);
                user.setPassword(password);
                user.setRole(role);
                user.setEmail(email);

                userService.createUser(user);

                if (groupName != null) {
                    Group group = groupService.getOrCreateGroup(groupName);
                    groupService.addUserToGroup(user, group);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
