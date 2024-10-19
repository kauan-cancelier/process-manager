package com.attus.processmanager.controller;

import com.attus.processmanager.dto.StakeholderSaveRequest;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import com.attus.processmanager.service.StakeholderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("stakeholders")
@RequiredArgsConstructor
public class StakeholderController {

    private final StakeholderService stakeholderService;

    @GetMapping
    public ResponseEntity<List<?>> findAll(@RequestParam(required = false) StakeholderType type) {
        return ResponseEntity.ok(stakeholderService.list(type));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StakeholderSaveRequest stakeholderSaveRequest) {
        try {
            Stakeholder stakeholder = stakeholderService.save(stakeholderSaveRequest.toModel());
            URI location = new URI("/stakeholders/" + stakeholder.getId());
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Stakeholder updatedStakeholder) {
        try {
            Stakeholder existingStakeholder = stakeholderService.getById(id);
            updatedStakeholder.setId(existingStakeholder.getId());
            Stakeholder editedStakeholder = stakeholderService.save(updatedStakeholder);
            URI location = new URI("/stakeholders/" + editedStakeholder.getId());
            return ResponseEntity.ok().location(location).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            Stakeholder toRemoveStakeholder = stakeholderService.getById(id);
            stakeholderService.remove(toRemoveStakeholder);
            return ResponseEntity.ok(toRemoveStakeholder);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            Stakeholder stakeholder = stakeholderService.getById(id);
            return ResponseEntity.ok(stakeholder);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
}
