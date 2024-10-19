package com.attus.processmanager.models;

import com.attus.processmanager.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "legal_processes")
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LegalProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private Long caseNumber;

    @Column(nullable = false)
    private LocalDateTime openingDate;

    @Column(nullable = false)
    private String caseDescription;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

}
