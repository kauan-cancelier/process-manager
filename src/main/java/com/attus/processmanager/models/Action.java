package com.attus.processmanager.models;

import com.attus.processmanager.models.enums.ActionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "actions")
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "legal_processes_id", nullable = false)
    private LegalProcess legalProcess;

    @Column(name = "type", length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType type;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "description")
    private String description;

}
