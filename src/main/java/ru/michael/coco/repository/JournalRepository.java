package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.Journal;
import ru.michael.coco.entity.JournalId;

@Repository
public interface JournalRepository extends JpaRepository<Journal, JournalId> {
}
