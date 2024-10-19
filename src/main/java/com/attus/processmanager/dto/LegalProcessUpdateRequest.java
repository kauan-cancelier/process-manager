package com.attus.processmanager.dto;

import lombok.Data;

@Data
public class LegalProcessUpdateRequest {

    private Long id;

    private Long caseNumber;

    private String caseDescription;

}
