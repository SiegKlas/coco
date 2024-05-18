package ru.michael.coco.bank.tree;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankLevelRepository extends JpaRepository<BankLevel, Long> {
}

