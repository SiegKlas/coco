package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.Group;
import ru.michael.coco.entity.GroupUser;
import ru.michael.coco.entity.GroupUserId;
import ru.michael.coco.entity.User;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUserId> {
    void deleteAllByGroup(Group group);

    List<GroupUser> findByGroup(Group group);

    void deleteByGroupAndUser(Group group, User user);
}
