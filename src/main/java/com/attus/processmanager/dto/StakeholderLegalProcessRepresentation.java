package com.attus.processmanager.dto;

import com.attus.processmanager.models.StakeholderLegalProcess;
import lombok.Data;

@Data
public class StakeholderLegalProcessRepresentation {

    private Long id;

    private Long stakeholderId;

    private Long legalProcessId;

    public StakeholderLegalProcessRepresentation(StakeholderLegalProcess stakeholderLegalProcess) {
        this.id = stakeholderLegalProcess.getId();
        this.stakeholderId = stakeholderLegalProcess.getStakeholder().getId();
        this.legalProcessId = stakeholderLegalProcess.getLegalProcess().getId();
    }

}
