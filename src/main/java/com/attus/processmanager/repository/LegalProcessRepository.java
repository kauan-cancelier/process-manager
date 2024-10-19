package com.attus.processmanager.repository;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.LegalProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LegalProcessRepository extends JpaRepository<LegalProcess, Long> {

    @Query("SELECT p FROM LegalProcess p WHERE p.id = :id")
    LegalProcess findBy(long id);

    boolean existsByNumber(Long number);

    @Query("SELECT p FROM LegalProcess p WHERE p.status = :status")
    List<LegalProcess> listBy(LegalProcessStatus status);

}
