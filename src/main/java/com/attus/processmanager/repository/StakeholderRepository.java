package com.attus.processmanager.repository;

import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StakeholderRepository extends JpaRepository<Stakeholder, Long> {

    @Query("SELECT s FROM Stakeholder s WHERE s.id = :id")
    Stakeholder findBy(Long id);

    @Query("SELECT s FROM Stakeholder s WHERE s.type = :type")
    List<Stakeholder> listBy(StakeholderType type);
}
