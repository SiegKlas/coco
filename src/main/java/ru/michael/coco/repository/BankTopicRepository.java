package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.BankTopic;
import ru.michael.coco.entity.BankTopicId;

@Repository
public interface BankTopicRepository extends JpaRepository<BankTopic, BankTopicId> {
}
