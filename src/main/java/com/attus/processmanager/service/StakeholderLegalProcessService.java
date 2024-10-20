package com.attus.processmanager.service;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.StakeholderLegalProcess;
import com.attus.processmanager.repository.StakeholderLegalProcessRepository;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StakeholderLegalProcessService {

    private final StakeholderLegalProcessRepository repository;

    public StakeholderLegalProcess save(StakeholderLegalProcess stakeholderLegalProcess) {
        Preconditions.checkNotNull(stakeholderLegalProcess, "The stakeholder legal process must not be null");
        return repository.save(stakeholderLegalProcess);
    }

    public void remove(StakeholderLegalProcess stakeholderLegalProcess) {
        Preconditions.checkNotNull(stakeholderLegalProcess, "The stakeholder legal process must not be null");
        repository.delete(stakeholderLegalProcess);
    }

    public StakeholderLegalProcess getById(Long id) {
        Preconditions.checkNotNull(id, "The id must not be null");
        StakeholderLegalProcess stakeholderLegalProcess = repository.findBy(id);
        Preconditions.checkNotNull(stakeholderLegalProcess, "Stakeholder legal process not found");
        return stakeholderLegalProcess;
    }

    public List<Stakeholder> listBy(LegalProcess legalProcess) {
        Preconditions.checkNotNull(legalProcess, "The legal process must not be null");
        return repository.listBy(legalProcess);
    }


}
