package com.attus.processmanager.controller;

import com.attus.processmanager.dto.LegalProcessDto;
import com.attus.processmanager.dto.LegalProcessSaveRequest;
import com.attus.processmanager.dto.LegalProcessUpdateRequest;
import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.LegalProcessStatus;
import com.attus.processmanager.service.ActionService;
import com.attus.processmanager.service.LegalProcessService;
import com.attus.processmanager.service.StakeholderLegalProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("legal-processes")
@RequiredArgsConstructor
public class LegalProcessController {

    private final LegalProcessService service;

    private final StakeholderLegalProcessService stakeholderLegalProcessService;

    private final ActionService actionService;

    @GetMapping
    public ResponseEntity<Object> list(@RequestParam(required = false) String status) {
        try {
            if (status != null && !status.isBlank()) {
                return ResponseEntity.ok(service.list(LegalProcessStatus.tryConvert(status)));
            }
            return ResponseEntity.ok(service.list(null));
        } catch (IllegalArgumentException e ) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody LegalProcessSaveRequest process) {
        try {
            LegalProcess legalProcess = service.save(process.toModel());
            return ResponseEntity.status(201).body(legalProcess);
        } catch (NullPointerException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody LegalProcessUpdateRequest updatedProcess) {
        try {
            LegalProcess existingProcess = service.getById(id);

            if (updatedProcess.getStatus() != null) {
                existingProcess.setStatus(LegalProcessStatus.tryConvert(updatedProcess.getStatus()));
            }
            existingProcess.setDescription(updatedProcess.getDescription());
            existingProcess.setNumber(updatedProcess.getNumber());
            LegalProcess editedLegalProcess = service.save(existingProcess);

            return ResponseEntity.ok().body(editedLegalProcess);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        try {
            LegalProcess toRemoveProcess = service.getById(id);
            service.remove(toRemoveProcess);
            return ResponseEntity.ok(toRemoveProcess);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        try {
            LegalProcess legalProcess = service.getById(id);
            List<Stakeholder> stakes = stakeholderLegalProcessService.listBy(legalProcess);
            List<Action> actions = actionService.listBy(legalProcess);
            LegalProcessDto processDto = new LegalProcessDto(legalProcess, stakes, actions);
            return ResponseEntity.ok(processDto);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/inactivate")
    public ResponseEntity<Object> inactivateProcess(@PathVariable("id") Long id) {
        try {
            LegalProcess inactivatedProcess = service.inactivateProcess(id);
            return ResponseEntity.ok(inactivatedProcess);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Object> activateProcess(@PathVariable("id") Long id) {
        try {
            LegalProcess inactivatedProcess = service.activateProcess(id);
            return ResponseEntity.ok(inactivatedProcess);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
