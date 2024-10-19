package com.attus.processmanager.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "stakeholder_legal_processes")
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StakeholderLegalProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stakeholder_id", nullable = false)
    private Stakeholder stakeholder;

    @ManyToOne
    @JoinColumn(name = "legal_process_id", nullable = false)
    private LegalProcess legalProcess;

}
