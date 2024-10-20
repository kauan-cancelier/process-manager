package com.attus.processmanager.dto;

import lombok.Data;

@Data
public class LegalProcessUpdateRequest {

    private Long id;

    private Long number;

    private String description;

    private String status;

}
