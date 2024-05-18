package ru.michael.coco.bank.tree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.bank.Bank;

import java.util.Optional;

@Service
public class BankStructureService {
    private final BankStructureRepository bankStructureRepository;

    @Autowired
    public BankStructureService(BankStructureRepository bankStructureRepository) {
        this.bankStructureRepository = bankStructureRepository;
    }

    public Optional<BankStructure> findById(Long id) {
        return bankStructureRepository.findById(id);
    }

    public Optional<BankStructure> findByBank(Bank bank) {
        return bankStructureRepository.findByBank(bank);
    }

    public void saveBankStructure(BankStructure bankStructure) {
        bankStructureRepository.save(bankStructure);
    }
}

