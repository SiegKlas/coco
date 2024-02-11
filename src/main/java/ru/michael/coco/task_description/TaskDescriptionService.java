package ru.michael.coco.task_description;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
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

    public Optional<TaskDescription> findTaskDescriptionByNumber(Integer number) {
        return taskDescriptionRepository.findTaskDescriptionByNumber(number);
    }

    public void saveTask(Path path) {
        final String fName = List.of(path.toString().split("\\\\")).getLast();

        Pattern pattern = Pattern.compile("NSU-T(\\d{2})L(\\d)e(\\d{2})");
        Matcher matcher = pattern.matcher(fName);

        if (matcher.find()) {
            String topic = matcher.group(1);
            String level = matcher.group(2);
            String number = matcher.group(3);

            ObjectMapper objectMapper = new ObjectMapper();
            Path metaPath = path.resolve("meta.json");
            try {
                MetaData metaData = objectMapper.readValue(Files.newBufferedReader(metaPath), MetaData.class);
                String topicName = metaData.getTopic();
                String taskName = metaData.getTask();
                taskDescriptionRepository.save(
                        new TaskDescription(fName, Integer.parseInt(number), taskName, path.toString())
                );

//                taskDescriptionRepository.save(new TaskDescription(fName, Integer.parseInt(topic), topicName,
//                        Integer.parseInt(level), Integer.parseInt(number), taskName, path.toString())
//                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Task description not found");
        }
    }

    @Data
    private static class MetaData {
        private String topic;
        private String task;
    }
}
