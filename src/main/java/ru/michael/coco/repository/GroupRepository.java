package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
