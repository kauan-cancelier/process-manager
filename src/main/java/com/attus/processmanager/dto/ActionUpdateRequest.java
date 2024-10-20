package com.attus.processmanager.dto;

import com.attus.processmanager.models.LegalProcess;
import lombok.Data;

@Data
public class ActionUpdateRequest {

    private Long id;

    private LegalProcess legalProcess;

    private String type;

    private String description;

}
