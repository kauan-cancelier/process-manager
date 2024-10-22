package com.attus.processmanager.service;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.LegalProcessStatus;
import com.attus.processmanager.repository.LegalProcessRepository;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegalProcessService {

    private final LegalProcessRepository legalProcessRepository;

    public LegalProcess save(LegalProcess process) {

        Preconditions.checkNotNull(process.getDescription(), "The description is required to save");
        Preconditions.checkNotNull(process.getNumber(), "The number is required to save");

        if (process.getId() == null) {
            Preconditions.checkArgument(!legalProcessRepository.existsByNumber(process.getNumber()), "Case number already exists");
        }

        if (process.getId() == null && process.getStatus() == null) {
            process.setStatus(LegalProcessStatus.ATIVO);
        }

        return legalProcessRepository.save(process);
    }

    public void remove(LegalProcess process) {
        legalProcessRepository.delete(process);
    }

    @Cacheable(value = "getLegalProcessById", key = "#id")
    public LegalProcess getById(Long id) {
        LegalProcess process = legalProcessRepository.findBy(id);
        Preconditions.checkNotNull(process, "Not found process");
        return process;
    }

    public List<LegalProcess> list(LegalProcessStatus legalProcessStatus) {
        if (legalProcessStatus == null) {
            return legalProcessRepository.findAll();
        }
        return legalProcessRepository.listBy(legalProcessStatus);

    }

    public LegalProcess inactivateProcess(Long id) {
        LegalProcess process = getById(id);
        return changeStatus(process, LegalProcessStatus.ARQUIVADO);

    }

    public LegalProcess activateProcess(Long id) {
        LegalProcess process = getById(id);
        return changeStatus(process, LegalProcessStatus.ATIVO);
    }

    private LegalProcess changeStatus(LegalProcess process, LegalProcessStatus legalProcessStatus) {
        if (process.getStatus() != legalProcessStatus) {
            process.setStatus(legalProcessStatus);
            return legalProcessRepository.save(process);
        }
        return process;
    }
}
