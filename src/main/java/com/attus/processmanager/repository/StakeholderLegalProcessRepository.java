package com.attus.processmanager.repository;

import com.attus.processmanager.models.StakeholderLegalProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StakeholderLegalProcessRepository extends JpaRepository<StakeholderLegalProcess, Long> {

    @Query("SELECT slp FROM StakeholderLegalProcess slp WHERE slp.id = :id")
   StakeholderLegalProcess findBy(Long id);

}

