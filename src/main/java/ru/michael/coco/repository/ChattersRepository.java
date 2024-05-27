package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.Chatters;
import ru.michael.coco.entity.ChattersId;

@Repository
public interface ChattersRepository extends JpaRepository<Chatters, ChattersId> {
}
