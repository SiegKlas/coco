package ru.michael.coco.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.group.Group;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {
    private final BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Bank> findAllBanks() {
        return bankRepository.findAll();
    }

    public Optional<Bank> findById(Long id) {
        return bankRepository.findById(id);
    }

    public void saveBank(Bank bank) {
        bankRepository.save(bank);
    }

    public void deleteById(Long id) {
        bankRepository.deleteById(id);
    }

    public List<Bank> findByGroup(Group group) {
        return bankRepository.findAllByGroup(group);
    }
}
