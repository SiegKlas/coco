package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class BankTopic {
    @EmbeddedId
    private BankTopicId id;

    @ManyToOne
    @MapsId("bankId")
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToOne
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    private Topic topic;
}
