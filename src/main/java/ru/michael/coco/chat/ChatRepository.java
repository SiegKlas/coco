package ru.michael.coco.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.user.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findByStudent(User student);
}
