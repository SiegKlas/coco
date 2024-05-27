package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.Solution;
import ru.michael.coco.repository.SolutionRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SolutionService {
    private final SolutionRepository solutionRepository;

    @Autowired
    public SolutionService(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    public Solution saveSolution(Solution solution) {
        return solutionRepository.save(solution);
    }

    public List<Solution> getAllSolutions() {
        return solutionRepository.findAll();
    }

    public void deleteSolutionById(Long solutionId) {
        solutionRepository.deleteById(solutionId);
    }

    public Optional<Solution> getSolutionById(Long solutionId) {
        return solutionRepository.findById(solutionId);
    }
}

