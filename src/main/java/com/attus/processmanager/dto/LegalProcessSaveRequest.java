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

    private Long number;

    private String description;

    public LegalProcess toModel() {
        return LegalProcess.builder()
                .description(description)
                .number(number)
                .openingDate(LocalDateTime.now())
                .build();
    }

}
