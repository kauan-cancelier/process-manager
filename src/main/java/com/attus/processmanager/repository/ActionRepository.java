package com.attus.processmanager.repository;

import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {

    @Query("SELECT a FROM Action a WHERE a.id = :id")
    Action findBy(Long id);

    @Query("SELECT a FROM Action a WHERE a.type = :type")
    List<Action> listBy(ActionType type);

    @Query("SELECT a FROM Action a WHERE a.legalProcess = :legalProcess")
    List<Action> findBy(LegalProcess legalProcess);
}
