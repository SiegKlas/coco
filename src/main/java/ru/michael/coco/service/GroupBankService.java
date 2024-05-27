package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.repository.GroupBankRepository;

@Service
@Transactional
public class GroupBankService {
    private final GroupBankRepository groupBankRepository;

    @Autowired
    public GroupBankService(GroupBankRepository groupBankRepository) {
        this.groupBankRepository = groupBankRepository;
    }

    // Методы сервиса для GroupBankRepository
}
