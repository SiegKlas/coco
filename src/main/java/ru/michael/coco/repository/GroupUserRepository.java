package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.GroupUser;
import ru.michael.coco.entity.GroupUserId;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUserId> {
}
