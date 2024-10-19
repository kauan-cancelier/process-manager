package com.attus.processmanager.dto;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.ActionType;
import lombok.Data;

@Data
public class ActionUpdateRequest {

    private Long id;

    private LegalProcess legalProcess;

    private ActionType type;

    private String description;

}
