package com.attus.processmanager.dto;

import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.ActionType;
import lombok.Data;

@Data
public class ActionSaveRequest {

    private LegalProcess legalProcess;

    private String type;

    private String description;

    public Action toModel() {
        return Action.builder()
                .legalProcess(legalProcess)
                .type(ActionType.tryConvert(type))
                .description(description)
                .build();

    }

}
