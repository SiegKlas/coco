package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.repository.BankTopicRepository;

@Service
@Transactional
public class BankTopicService {
    private final BankTopicRepository bankTopicRepository;

    @Autowired
    public BankTopicService(BankTopicRepository bankTopicRepository) {
        this.bankTopicRepository = bankTopicRepository;
    }

}
