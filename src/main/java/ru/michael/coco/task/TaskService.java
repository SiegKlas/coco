package ru.michael.coco.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.util.List;
import java.util.Optional;

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

    /**
     * @param task The current task
     * @return The next task of the same level or the first task of the next level. Only for the same topic!
     */
    public Optional<Task> findNextTask(Task task) {
        int topic = task.getTaskDescription().getTopic();
        int level = task.getTaskDescription().getLevel();
        int number = task.getTaskDescription().getNumber();

        List<Task> sameTopic = findTasksByUser(task.getUser()).stream()
                .filter(t -> t.getTaskDescription().getTopic().equals(topic))
                .toList();
        List<Task> sameLevel = sameTopic.stream()
                .filter(t -> t.getTaskDescription().getLevel().equals(level))
                .toList();
        if (sameLevel.stream().anyMatch(t -> t.getTaskDescription().getNumber().equals(number + 1))) {
            return sameLevel.stream().filter(t -> t.getTaskDescription().getNumber().equals(number + 1)).findFirst();
        }
        List<Task> nextLevel = sameTopic.stream()
                .filter(t -> t.getTaskDescription().getLevel().equals(level + 1))
                .toList();
        if (!nextLevel.isEmpty()) {
            return nextLevel.stream().filter(t -> t.getTaskDescription().getNumber().equals(1)).findFirst();
        }
        return Optional.empty();
    }

    public Boolean isLevelLocked(UserDetails userDetails, Integer topic, Integer level) {
        List<Task> tasksByUser = findTasksByUser(userDetails);
        List<Task> sameTopic = findTasksByUser(tasksByUser.getFirst().getUser()).stream()
                .filter(t -> t.getTaskDescription().getTopic().equals(topic))
                .toList();
        List<Task> sameLevel = sameTopic.stream()
                .filter(t -> t.getTaskDescription().getLevel().equals(level))
                .toList();
        return sameLevel.stream().allMatch(Task::getIsLocked);
    }

    public void save(Task task) {
        // логика разблокировки заданий
        taskRepository.save(task);
        if (task.getStatus().equals(2)) {
            findNextTask(task).ifPresent(n -> n.setIsLocked(false));
        }
    }
}
