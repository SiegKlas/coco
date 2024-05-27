package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TopicSchedule {
    @EmbeddedId
    private TopicScheduleId id;

    @ManyToOne
    @MapsId("scheduleId")
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}
