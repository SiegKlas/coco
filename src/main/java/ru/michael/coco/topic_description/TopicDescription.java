package ru.michael.coco.topic_description;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TopicDescription {
    private final Integer number;
    private final String name;
    private final String named;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
