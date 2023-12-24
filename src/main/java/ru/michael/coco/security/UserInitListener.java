package ru.michael.coco.security;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.michael.coco.user.UserEntity;
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
        if (userRepository.findByUsername("user").isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("123"));
            user.setRole(UserEntity.Role.USER);
            userRepository.save(user);
        }
        if (userRepository.findByUsername("admin").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setRole(UserEntity.Role.ADMIN);
            userRepository.save(admin);
        }
    }
}
