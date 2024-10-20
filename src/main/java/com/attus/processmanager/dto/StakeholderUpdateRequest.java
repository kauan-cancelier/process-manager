package com.attus.processmanager.dto;

import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import lombok.Data;

import java.util.Optional;

@Data
public class StakeholderUpdateRequest {

    private Long id;

    private String name;

    private String cpf;

    private String cnpj;

    private String type;

    private String email;

    private String phone;

    public Stakeholder updateModel(Stakeholder stakeholder) throws IllegalArgumentException {

        if (stakeholder.getCpf() != null && cnpj != null) {
            throw new IllegalArgumentException("The CPF cannot be changed to CNPJ.");
        }

        if (stakeholder.getCnpj() != null && cpf != null) {
            throw new IllegalArgumentException("The CNPJ cannot be changed to CPF");
        }

        Optional.ofNullable(cpf).ifPresent(stakeholder::setCpf);
        Optional.ofNullable(cnpj).ifPresent(stakeholder::setCnpj);
        Optional.ofNullable(name).ifPresent(stakeholder::setName);
        Optional.ofNullable(email).ifPresent(stakeholder::setEmail);
        Optional.ofNullable(phone).ifPresent(stakeholder::setPhone);

        if (type != null) {
            stakeholder.setType(StakeholderType.tryConvert(type));
        }

        return stakeholder;
    }
}
