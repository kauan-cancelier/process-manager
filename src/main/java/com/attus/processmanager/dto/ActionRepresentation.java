package com.attus.processmanager.dto;

import com.attus.processmanager.models.Action;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionRepresentation {

    private Long id;

    private Long legalProcessId;

    private String type;

    private String description;

    public static ActionRepresentation fromModel(Action action) {
        return ActionRepresentation.builder()
                .id(action.getId())
                .legalProcessId(action.getLegalProcess().getId())
                .type(action.getType().toString())
                .description(action.getDescription())
                .build();
    }

}
