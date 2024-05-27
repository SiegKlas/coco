package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}