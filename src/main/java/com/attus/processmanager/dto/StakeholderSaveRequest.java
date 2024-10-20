package com.attus.processmanager.dto;

import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import lombok.Data;

@Data
public class StakeholderSaveRequest {

    private String name;

    private String cpf;

    private String cnpj;

    private String type;

    private String email;

    private String phone;

    public Stakeholder toModel() {
        return Stakeholder.builder()
                .name(name)
                .cpf(cpf)
                .cnpj(cnpj)
                .type(StakeholderType.tryConvert(type))
                .email(email)
                .phone(phone)
                .build();

    }

}
