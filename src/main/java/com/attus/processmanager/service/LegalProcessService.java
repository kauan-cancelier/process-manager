package com.attus.processmanager.service;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.Status;
import com.attus.processmanager.repository.LegalProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegalProcessService {

    private final LegalProcessRepository legalProcessRepository;

    public LegalProcess save(LegalProcess process) {
        if (legalProcessRepository.existsByCaseNumber(process.getCaseNumber())) {
            throw new IllegalArgumentException("Case number already exists");
        }
        if (process.getStatus() == null) {
            process.setStatus(Status.ATIVO);
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
        return legalProcessRepository.findBy(id);
    }

    public List<LegalProcess> list(Status status) {
        if (status == null) {
            return legalProcessRepository.findAll();
        }
        return legalProcessRepository.listBy(status);

    }

    public LegalProcess inactivateProcess(Long id) {
        LegalProcess process = legalProcessRepository.findBy(id);
        return changeStatus(process, Status.ARQUIVADO);

    }

    public LegalProcess activateProcess(Long id) {
        LegalProcess process = legalProcessRepository.findBy(id);
        return changeStatus(process, Status.ATIVO);
    }

    private LegalProcess changeStatus(LegalProcess process, Status status) {
        if (process.getStatus() != status) {
            process.setStatus(status);
            return legalProcessRepository.save(process);
        }
        return process;
    }
}
