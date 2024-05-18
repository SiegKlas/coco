package ru.michael.coco.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.michael.coco.group.Group;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {
    private final BankRepository bankRepository;
    @Value("${file.groups-dir}")
    private String groupsDir;

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
        createBankDirectory(bank.getGroup().getId(), bank.getName());
    }

    public void deleteById(Long id) {
        //Bank bank = bankRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank not found"));
        bankRepository.deleteById(id);
    }

    private void createBankDirectory(Long groupId, String bankName) {
        File bankDir = new File(groupsDir + "/" + groupId, bankName);
        if (!bankDir.exists()) {
            bankDir.mkdirs();
        }
    }

    public List<Bank> findByGroup(Group group) {
        return bankRepository.findAllByGroup(group);
    }
}
