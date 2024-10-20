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

import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity<List<LegalProcess>> list(@RequestParam(required = false) String status) {
        LegalProcessStatus legalProcessStatus = null;
        if (status != null) {
            try {
                legalProcessStatus = LegalProcessStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid option for process status: {}", e.getMessage());
            }
        }
        return ResponseEntity.ok(service.list(legalProcessStatus));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody LegalProcessSaveRequest process) {
        try {
            LegalProcess legalProcess = service.save(process.toModel());
            URI location = new URI("/legal-processes/" + legalProcess.getId());
            return ResponseEntity.created(location).build();

        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("Error creating LegalProcess: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (URISyntaxException e) {
            log.error("URI Syntax Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody LegalProcessUpdateRequest updatedProcess) {
        try {
            LegalProcess existingProcess = service.getById(id);

            LegalProcessStatus legalProcessStatus = existingProcess.getStatus();

            existingProcess.setDescription(updatedProcess.getDescription());
            existingProcess.setNumber(updatedProcess.getNumber());

            if (updatedProcess.getStatus() != null) {
                try {
                    legalProcessStatus = LegalProcessStatus.valueOf(updatedProcess.getStatus().toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid option for process status: {}", e.getMessage());
                    return ResponseEntity.badRequest().body("Invalid option for process status");
                }
            }

            existingProcess.setStatus(legalProcessStatus);
            LegalProcess editedLegalProcess = service.save(existingProcess);

            URI location = new URI("/legal-processes/" + editedLegalProcess.getId());
            return ResponseEntity.ok().location(location).build();
        } catch (Exception e) {
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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
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
