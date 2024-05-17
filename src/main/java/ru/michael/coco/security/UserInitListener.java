package ru.michael.coco.security;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserRepository;

//@Component
public class UserInitListener implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //@Autowired
    public UserInitListener(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
//        User admin = new User("admin");
//        admin.setPassword(passwordEncoder.encode("123"));
//        admin.setRole(User.Role.TEACHER);
//
//        if (userRepository.findByUsername("user").isEmpty()) {
//            User user = new User("user");
//            user.setPassword(passwordEncoder.encode("123"));
//            user.setRole(User.Role.STUDENT);
//            user.setTeacher(admin);
//            userRepository.save(user);
//        }
//
//        if (userRepository.findByUsername("admin").isEmpty()) {
//            userRepository.save(admin);
//        }
        User overseer = new User();
        overseer.setUsername("overseer");
        overseer.setRole(User.Role.OVERSEER);
        overseer.setPassword(passwordEncoder.encode("123"));
    }
}
