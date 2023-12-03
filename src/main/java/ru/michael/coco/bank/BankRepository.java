package ru.michael.coco.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.michael.coco.group.Group;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findAllByGroup(Group group);
}

