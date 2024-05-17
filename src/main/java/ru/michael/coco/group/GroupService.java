package ru.michael.coco.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final String groupsDir;

    @Autowired
    public GroupService(GroupRepository groupRepository, @Value("${file.groups-dir}") String groupsDir) {
        this.groupRepository = groupRepository;
        this.groupsDir = groupsDir;
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
        createGroupDirectory(group.getId());
    }

    public void deleteGroupById(Long id) {
        groupRepository.deleteById(id);
        deleteGroupDirectory(id);
    }

    public Optional<Group> findByName(String name) {
        return groupRepository.findByName(name);
    }

    private void createGroupDirectory(Long groupId) {
        Path groupPath = Paths.get(groupsDir, String.valueOf(groupId));
        try {
            if (!Files.exists(groupPath)) {
                Files.createDirectories(groupPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for group " + groupId, e);
        }
    }

    private void deleteGroupDirectory(Long groupId) {
        Path groupPath = Paths.get(groupsDir, String.valueOf(groupId));
        try {
            if (Files.exists(groupPath)) {
                Files.walk(groupPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete directory for group " + groupId, e);
        }
    }
}

