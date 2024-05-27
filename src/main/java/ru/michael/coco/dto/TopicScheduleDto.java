package ru.michael.coco.dto;

import lombok.Data;

@Data
public class TopicScheduleDto {
    private Long topicId;
    private Long userId;
    private Long scheduleId;
}
