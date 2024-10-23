package com.attus.processmanager.repository;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.StakeholderLegalProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StakeholderLegalProcessRepository extends JpaRepository<StakeholderLegalProcess, Long> {

    @Query("SELECT slp FROM StakeholderLegalProcess slp WHERE slp.id = :id")
    StakeholderLegalProcess findBy(Long id);

    @Query("SELECT s " +
            "FROM StakeholderLegalProcess slp " +
            "JOIN Stakeholder s ON s.id = slp.stakeholder.id " +
            "WHERE slp.legalProcess = :legalProcess")
    List<Stakeholder> listBy(LegalProcess legalProcess);


    @Query("SELECT COUNT(s) > 0 " +
            "FROM StakeholderLegalProcess s " +
            "WHERE s.stakeholder = :stakeholder " +
            "AND s.legalProcess = :legalProcess")
    boolean checkIfExists(Stakeholder stakeholder, LegalProcess legalProcess);
}

