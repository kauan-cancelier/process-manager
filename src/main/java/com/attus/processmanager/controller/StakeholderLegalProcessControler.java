package com.attus.processmanager.controller;

import com.attus.processmanager.dto.StakeholderLegalProcessRepresentation;
import com.attus.processmanager.models.StakeholderLegalProcess;
import com.attus.processmanager.service.StakeholderLegalProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stakeholders-legal-process")
@RequiredArgsConstructor
public class StakeholderLegalProcessControler {

    private final StakeholderLegalProcessService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody StakeholderLegalProcess stakeholderLegalProcess) {
        try {
            StakeholderLegalProcess savedStake = service.save(stakeholderLegalProcess);
            StakeholderLegalProcessRepresentation stake = new StakeholderLegalProcessRepresentation(savedStake);
            return ResponseEntity.status(201).body(stake);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        try {
            StakeholderLegalProcess toRemoveStake = service.getById(id);
            service.remove(toRemoveStake);
            return ResponseEntity.ok(toRemoveStake);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
