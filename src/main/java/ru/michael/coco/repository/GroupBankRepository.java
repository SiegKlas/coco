package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.GroupBank;
import ru.michael.coco.entity.GroupBankId;

@Repository
public interface GroupBankRepository extends JpaRepository<GroupBank, GroupBankId> {
}
