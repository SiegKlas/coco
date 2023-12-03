package ru.michael.coco.bank.tree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankTopicService {
    private final BankTopicRepository bankTopicRepository;

    @Autowired
    public BankTopicService(BankTopicRepository bankTopicRepository) {
        this.bankTopicRepository = bankTopicRepository;
    }

    public Optional<BankTopic> findById(Long id) {
        return bankTopicRepository.findById(id);
    }

    public void saveBankTopic(BankTopic bankTopic) {
        bankTopicRepository.save(bankTopic);
    }
}

