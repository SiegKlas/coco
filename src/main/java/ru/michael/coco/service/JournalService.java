package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.Journal;
import ru.michael.coco.entity.JournalId;
import ru.michael.coco.repository.JournalRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JournalService {
    private final JournalRepository journalRepository;

    @Autowired
    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public Journal createJournal(Journal journal) {
        // Логика, если необходимо, перед сохранением записи в журнал
        return journalRepository.save(journal);
    }

    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public Optional<Journal> getJournalById(JournalId id) {
        return journalRepository.findById(id);
    }

    public Journal updateJournal(Journal journal) {
        // Логика обновления записи в журнале, если необходимо
        return journalRepository.save(journal);
    }

    public void deleteJournalById(JournalId id) {
        journalRepository.deleteById(id);
    }
}
