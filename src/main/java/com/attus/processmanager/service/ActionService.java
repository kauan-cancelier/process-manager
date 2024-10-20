package com.attus.processmanager.service;

import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.ActionType;
import com.attus.processmanager.repository.ActionRepository;
import com.google.common.base.Preconditions;
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
        Preconditions.checkNotNull(action, "Action must not be null");
        Preconditions.checkNotNull(action.getLegalProcess(), "Legal process must not be null");
        Preconditions.checkNotNull(action.getLegalProcess().getId(), "Legal process ID must not be null");

        legalProcessService.getById(action.getLegalProcess().getId());

        if (action.getRegistrationDate() == null) {
            action.setRegistrationDate(LocalDateTime.now());
        }

        return actionRepository.save(action);
    }

    public void remove(Action action) {
        Preconditions.checkNotNull(action, "Action must not be null");
        actionRepository.delete(action);
    }

    public Action getById(Long id) {
        Preconditions.checkNotNull(id, "Action ID must not be null");
        Action action = actionRepository.findBy(id);
        Preconditions.checkNotNull(action, "Action not found");
        return action;
    }

    public List<Action> list(ActionType type) {
        if (type == null) {
            return actionRepository.findAll();
        }
        return actionRepository.listBy(type);
    }

    public List<Action> listBy(LegalProcess legalProcess) {
        Preconditions.checkNotNull(legalProcess, "Legal process must not be null");
        return actionRepository.findBy(legalProcess);
    }

}
