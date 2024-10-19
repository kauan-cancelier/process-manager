package com.attus.processmanager.service;

import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import com.attus.processmanager.repository.StakeholderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StakeholderService {

    private final StakeholderRepository stakeholderRepository;

    public Stakeholder save(Stakeholder stakeholder) {
        return stakeholderRepository.save(stakeholder);
    }

    public void remove(Stakeholder stakeholder) {
        stakeholderRepository.delete(stakeholder);
    }

    public Stakeholder getById(Long id) {
        Stakeholder stakeholder = stakeholderRepository.findBy(id);
        if (stakeholder == null) {
            throw new IllegalArgumentException("Stakeholder not found");
        }
        return stakeholder;
    }

    public List<Stakeholder> list(StakeholderType type) {
        if (type == null) {
            return stakeholderRepository.findAll();
        }
        return stakeholderRepository.listBy(type);
    }

}
