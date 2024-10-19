package com.attus.processmanager.models;

import com.attus.processmanager.models.enums.StakeholderType;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "stakeholders")
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stakeholder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cpf", length = 11)
    private String cpf;

    @Column(name = "cnpj", length = 14)
    private String cnpj;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StakeholderType type;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;
}
