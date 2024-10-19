package com.attus.processmanager.dto;

import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.Stakeholder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class LegalProcessDto {

    LegalProcess legalProcess;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Stakeholder> stakeholders;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Action> actions;

}
