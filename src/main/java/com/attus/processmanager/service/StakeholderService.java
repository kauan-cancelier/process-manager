package com.attus.processmanager.service;

import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import com.attus.processmanager.repository.StakeholderRepository;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StakeholderService {

    private final StakeholderRepository stakeholderRepository;

    public Stakeholder save(Stakeholder stakeholder) {
        Preconditions.checkNotNull(stakeholder, "Stakeholder must not be null");

        if (stakeholder.getCpf() == null && stakeholder.getCnpj() == null) {
            throw new IllegalArgumentException("The CPF or CNPJ must be provided.");
        }

        if (stakeholder.getCpf() != null && stakeholder.getCnpj() != null) {
            throw new IllegalArgumentException("The CPF and CNPJ cannot be provided together.");
        }
        return stakeholderRepository.save(stakeholder);
    }

    public void remove(Stakeholder stakeholder) {
        Preconditions.checkNotNull(stakeholder, "Stakeholder must not be null");
        stakeholderRepository.delete(stakeholder);
    }

    public Stakeholder getById(Long id) {
        Preconditions.checkNotNull(id, "Id must not be null");
        Stakeholder stakeholder = stakeholderRepository.findBy(id);
        Preconditions.checkNotNull(stakeholder, "Stakeholder not found");
        return stakeholder;
    }

    public List<Stakeholder> list(StakeholderType type) {
        if (type == null) {
            return stakeholderRepository.findAll();
        }
        return stakeholderRepository.listBy(type);
    }

}
