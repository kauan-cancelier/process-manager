package com.attus.processmanager.controller;

import com.attus.processmanager.models.StakeholderLegalProcess;
import com.attus.processmanager.service.StakeholderLegalProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("stakeholders-legal-process")
@RequiredArgsConstructor
public class StakeholderLegalProcessControler {

    private final StakeholderLegalProcessService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody StakeholderLegalProcess stakeholderLegalProcess) {
        try {
            StakeholderLegalProcess savedStake = service.save(stakeholderLegalProcess);
            URI location = new URI("/stakeholders-legal-process/" + savedStake.getId());
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        try {
            StakeholderLegalProcess toRemoveStake = service.getById(id);
            service.remove(toRemoveStake);
            return ResponseEntity.ok(toRemoveStake);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
