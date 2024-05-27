package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.Bank;
import ru.michael.coco.repository.BankRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BankService {
    private final BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Bank createBank(Bank bank) {
        // Дополнительная логика перед сохранением, если необходимо
        return bankRepository.save(bank);
    }

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Optional<Bank> getBankById(Long id) {
        return bankRepository.findById(id);
    }

    public Bank updateBank(Bank bank) {
        // Дополнительная логика обновления, если необходимо
        return bankRepository.save(bank);
    }

    public void deleteBankById(Long id) {
        bankRepository.deleteById(id);
    }

    // Другие методы могут быть добавлены здесь, если необходимо
}
