package ru.michael.coco.task_description;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TaskDescriptionService {
    private final TaskDescriptionRepository taskDescriptionRepository;

    @Autowired
    public TaskDescriptionService(TaskDescriptionRepository taskDescriptionRepository) {
        this.taskDescriptionRepository = taskDescriptionRepository;
    }

    public void saveTask(Path path) {
        final String fName = List.of(path.toString().split("\\\\")).getLast();

        Pattern pattern = Pattern.compile("NSU-T(\\d{2})L(\\d)e(\\d{2})");
        Matcher matcher = pattern.matcher(fName);

        if (matcher.find()) {
            String topic = matcher.group(1);
            String level = matcher.group(2);
            String number = matcher.group(3);

            taskDescriptionRepository.save(new TaskDescription(fName, Integer.parseInt(topic), Integer.parseInt(level)
                    , Integer.parseInt(number), path.toString()));
        } else {
            throw new RuntimeException("Task description not found");
        }
    }

    public List<Integer> getAllTopics() {
        return taskDescriptionRepository.findAll().stream().map(TaskDescription::getTopic).distinct().sorted().toList();
    }

    public List<Integer> getLevelsForTopic(Integer topic) {
        return taskDescriptionRepository.getLevelsForTopic(topic).stream().sorted().toList();
    }

    public List<Integer> getNumbersForTopicAndLevel(Integer topic, Integer level) {
        return taskDescriptionRepository.getNumbersForTopicAndLevel(topic, level).stream().sorted().toList();
    }

    public Optional<TaskDescription> getTaskDescription(Integer topic, Integer level, Integer number) {
        return taskDescriptionRepository.getTaskDescription(topic, level, number);
    }

    public List<TaskDescription> getTaskDescriptions(Integer topic, Integer level) {
        return taskDescriptionRepository.findTaskDescriptionsByTopicAndLevel(topic, level);
    }
}
