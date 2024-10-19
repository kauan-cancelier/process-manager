package com.attus.processmanager.controller;

import com.attus.processmanager.dto.LegalProcessSaveRequest;
import com.attus.processmanager.dto.LegalProcessUpdateRequest;
import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.models.enums.Status;
import com.attus.processmanager.service.LegalProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("legal-processes")
@RequiredArgsConstructor
public class LegalProcessController {

    private final LegalProcessService service;

    @GetMapping
    public ResponseEntity<List<LegalProcess>> findAll(@RequestParam(required = false) Status status) {
        return ResponseEntity.ok(service.list(status));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody LegalProcessSaveRequest process) {
        try {
            LegalProcess legalProcess = service.save(process.toModel());
            URI location = new URI("/legal-processes/" + legalProcess.getId());
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LegalProcessUpdateRequest updatedProcess) {
        try {
            LegalProcess existingProcess = service.getById(id);

            existingProcess.setCaseDescription(updatedProcess.getCaseDescription());
            existingProcess.setCaseNumber(updatedProcess.getCaseNumber());
            LegalProcess editedLegalProcess = service.save(existingProcess);

            URI location = new URI("/legal-processes/" + editedLegalProcess.getId());
            return ResponseEntity.ok().location(location).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            LegalProcess toRemoveProcess = service.getById(id);
            service.remove(toRemoveProcess);
            return ResponseEntity.ok(toRemoveProcess);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            LegalProcess legalProcess = service.getById(id);
            return ResponseEntity.ok(legalProcess);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/inactivate")
    public ResponseEntity<?> inactivateProcess(@PathVariable("id") Long id) {
        try {
            LegalProcess inactivatedProcess = service.inactivateProcess(id);
            return ResponseEntity.ok(inactivatedProcess);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateProcess(@PathVariable("id") Long id) {
        try {
            LegalProcess inactivatedProcess = service.activateProcess(id);
            return ResponseEntity.ok(inactivatedProcess);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
