package com.attus.processmanager.service;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.LegalProcessStatus;
import com.attus.processmanager.repository.LegalProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegalProcessService {

    private final LegalProcessRepository legalProcessRepository;

    public LegalProcess save(LegalProcess process) {
        if (legalProcessRepository.existsByNumber(process.getNumber())) {
            throw new IllegalArgumentException("Case number already exists");
        }
        if (process.getStatus() == null) {
            process.setStatus(LegalProcessStatus.ATIVO);
        }
        return legalProcessRepository.save(process);
    }

    public void remove(LegalProcess process) {
        legalProcessRepository.delete(process);
    }

    public LegalProcess getById(Long id) {
        LegalProcess process = legalProcessRepository.findBy(id);
        if (process == null) {
            throw new IllegalArgumentException("Process not found");
        }
        return process;
    }

    public List<LegalProcess> list(LegalProcessStatus legalProcessStatus) {
        if (legalProcessStatus == null) {
            return legalProcessRepository.findAll();
        }
        return legalProcessRepository.listBy(legalProcessStatus);

    }

    public LegalProcess inactivateProcess(Long id) {
        LegalProcess process = legalProcessRepository.findBy(id);
        return changeStatus(process, LegalProcessStatus.ARQUIVADO);

    }

    public LegalProcess activateProcess(Long id) {
        LegalProcess process = legalProcessRepository.findBy(id);
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