package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.repository.GroupUserRepository;

@Service
@Transactional
public class GroupUserService {
    private final GroupUserRepository groupUserRepository;

    @Autowired
    public GroupUserService(GroupUserRepository groupUserRepository) {
        this.groupUserRepository = groupUserRepository;
    }

}
