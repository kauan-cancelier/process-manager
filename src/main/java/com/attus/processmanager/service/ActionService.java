package com.attus.processmanager.service;

import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.enums.ActionType;
import com.attus.processmanager.repository.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionService {

    private final ActionRepository actionRepository;

    private final LegalProcessService legalProcessService;

    public Action save(Action action) {
        legalProcessService.getById(action.getLegalProcess().getId());
        if (action.getRegistrationDate() == null) {
            action.setRegistrationDate(LocalDateTime.now());
        }
        return actionRepository.save(action);
    }

    public void remove(Action action) {
        actionRepository.delete(action);
    }

    public Action getById(Long id) {
        Action action = actionRepository.findBy(id);
        if (action == null) {
            throw new IllegalArgumentException("Action not found");
        }
        return action;
    }

    public List<Action> list(ActionType type) {
        if (type == null) {
            return actionRepository.findAll();
        }
        return actionRepository.listBy(type);

    }

}
