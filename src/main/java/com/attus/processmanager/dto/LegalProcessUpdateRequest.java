package com.attus.processmanager.dto;

import com.attus.processmanager.models.enums.LegalProcessStatus;
import lombok.Data;

@Data
public class LegalProcessUpdateRequest {

    private Long id;

    private Long number;

    private String description;

    private String status;

}
