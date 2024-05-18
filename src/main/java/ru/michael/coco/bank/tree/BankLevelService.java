package ru.michael.coco.bank.tree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankLevelService {
    private final BankLevelRepository bankLevelRepository;

    @Autowired
    public BankLevelService(BankLevelRepository bankLevelRepository) {
        this.bankLevelRepository = bankLevelRepository;
    }

    public Optional<BankLevel> findById(Long id) {
        return bankLevelRepository.findById(id);
    }

    public void saveBankLevel(BankLevel bankLevel) {
        bankLevelRepository.save(bankLevel);
    }
}

