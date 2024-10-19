package com.attus.processmanager.dto;

import com.attus.processmanager.models.LegalProcess;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LegalProcessSaveRequest {

    private Long caseNumber;

    private String caseDescription;

    public LegalProcess toModel() {
        return LegalProcess.builder()
                .caseDescription(caseDescription)
                .caseNumber(caseNumber)
                .openingDate(LocalDateTime.now())
                .build();
    }

}
