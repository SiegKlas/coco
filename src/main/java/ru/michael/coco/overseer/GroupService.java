package ru.michael.coco.overseer;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    public Optional<Group> findById(@NonNull Long id) {
        return groupRepository.findById(id);
    }
}
