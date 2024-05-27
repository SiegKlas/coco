package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.Solution;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
}
