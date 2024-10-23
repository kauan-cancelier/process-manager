package com.attus.processmanager.dto;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.Stakeholder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class StakeholderRepresentation {

    private Long id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String cpf;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String cnpj;

    private String type;

    private String email;

    private String phone;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<LegalProcess> legalProcessList;

    public StakeholderRepresentation(Stakeholder stakeholder, List<LegalProcess> legalProcessList) {
        this.id = stakeholder.getId();
        this.name = stakeholder.getName();
        this.cpf = stakeholder.getCpf();
        this.cnpj = stakeholder.getCnpj();
        this.type = stakeholder.getType().toString();
        this.email = stakeholder.getEmail();
        this.phone = stakeholder.getPhone();
        this.legalProcessList = legalProcessList;
    }
}
