package com.attus.processmanager.controller;

import com.attus.processmanager.dto.StakeholderRepresentation;
import com.attus.processmanager.dto.StakeholderSaveRequest;
import com.attus.processmanager.dto.StakeholderUpdateRequest;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import com.attus.processmanager.service.StakeholderLegalProcessService;
import com.attus.processmanager.service.StakeholderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stakeholders")
@RequiredArgsConstructor
public class StakeholderController {

    private final StakeholderService stakeholderService;

    private final StakeholderLegalProcessService stakeholderLegalProcessService;

    @GetMapping
    public ResponseEntity<Object> list(@RequestParam(required = false) String type) {
        try {
            if (type != null && !type.isBlank()) {
                return ResponseEntity.ok(stakeholderService.list(StakeholderType.tryConvert(type)));
            }
            return ResponseEntity.ok(stakeholderService.list(null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody StakeholderSaveRequest stakeholderSaveRequest) {
        try {
            Stakeholder stakeholder = stakeholderService.save(stakeholderSaveRequest.toModel());
            return ResponseEntity.status(201).body(stakeholder);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody StakeholderUpdateRequest updatedStakeholder) {
        try {
            Stakeholder existingStakeholder = stakeholderService.getById(id);
            existingStakeholder = updatedStakeholder.updateModel(existingStakeholder);
            Stakeholder editedStakeholder = stakeholderService.save(existingStakeholder);

            return ResponseEntity.ok().body(editedStakeholder);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        try {
            Stakeholder toRemoveStakeholder = stakeholderService.getById(id);
            stakeholderService.remove(toRemoveStakeholder);
            return ResponseEntity.ok(toRemoveStakeholder);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        try {
            Stakeholder stakeholder = stakeholderService.getById(id);
            List<LegalProcess> list = stakeholderLegalProcessService.listBy(stakeholder);
            StakeholderRepresentation stakeholderRepresentation = new StakeholderRepresentation(stakeholder, list);
            return ResponseEntity.ok(stakeholderRepresentation);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
