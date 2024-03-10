package ru.michael.coco.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }

    public void save(User user) {
        this.userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
