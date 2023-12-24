package ru.michael.coco.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> findTasksByUser(UserDetails userDetails) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return taskRepository.findTasksByUser(user);
    }
}
