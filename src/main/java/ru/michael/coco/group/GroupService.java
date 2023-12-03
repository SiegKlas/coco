package ru.michael.coco.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.bank.Bank;
import ru.michael.coco.bank.BankService;
import ru.michael.coco.bank.tree.BankStructureService;
import ru.michael.coco.user.User;

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
    private final BankStructureService bankStructureService;
    private final BankService bankService;

    @Autowired
    public GroupService(GroupRepository groupRepository, @Value("${file.groups-dir}") String groupsDir, BankStructureService bankStructureService, BankService bankService) {
        this.groupRepository = groupRepository;
        this.groupsDir = groupsDir;
        this.bankStructureService = bankStructureService;
        this.bankService = bankService;
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    public List<Group> findGroupsByTeacher(User teacher) {
        return groupRepository.findAllByTeacher(teacher);
    }

    @Transactional
    public void setActiveBank(Long groupId, Long bankId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        Bank bank = group.getBanks().stream().filter(b -> b.getId().equals(bankId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Bank not found in group"));
        group.setActiveBank(bank);
        groupRepository.save(group);
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
        createGroupDirectory(group.getId());
    }

    public void deleteGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        for (Bank bank : group.getBanks()) {
            bankStructureService.deleteByBank(bank);
            bankService.deleteById(bank.getId());
        }
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

