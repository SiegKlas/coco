package ru.michael.coco.bank.tree;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.bank.Bank;

import java.util.Optional;

@Repository
public interface BankStructureRepository extends JpaRepository<BankStructure, Long> {
    Optional<BankStructure> findByBank(Bank bank);
}
