package com.attus.processmanager.service;

import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.StakeholderLegalProcess;
import com.attus.processmanager.repository.StakeholderLegalProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StakeholderLegalProcessService {

    private final StakeholderLegalProcessRepository repository;

    public StakeholderLegalProcess save(StakeholderLegalProcess stakeholderLegalProcess) {
       return repository.save(stakeholderLegalProcess);
    }

    public void remove(StakeholderLegalProcess stakeholderLegalProcess) {
        repository.delete(stakeholderLegalProcess);
    }

    public StakeholderLegalProcess getById(Long id) {
        StakeholderLegalProcess stakeholderLegalProcess = repository.findBy(id);
        if (stakeholderLegalProcess == null) {
            throw new IllegalArgumentException("Stakeholder legal process not found");
        }
        return stakeholderLegalProcess;
    }

}
