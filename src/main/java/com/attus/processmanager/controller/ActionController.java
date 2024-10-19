package com.attus.processmanager.controller;

import com.attus.processmanager.dto.ActionSaveRequest;
import com.attus.processmanager.dto.ActionUpdateRequest;
import com.attus.processmanager.models.Action;
import com.attus.processmanager.models.enums.ActionType;
import com.attus.processmanager.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @GetMapping
    public ResponseEntity<List<Action>> findAll(@RequestParam(required = false)ActionType type) {
        return ResponseEntity.ok(actionService.list(type));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ActionSaveRequest actionSaveRequest) {
        try {
            Action action = actionService.save(actionSaveRequest.toModel());
            URI location = new URI("/actions/" + action.getId());
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ActionUpdateRequest updatedAction) {
        try {
            Action existingAction = actionService.getById(id);

            existingAction.setDescription(updatedAction.getDescription());
            existingAction.setType(updatedAction.getType());
            existingAction.setLegalProcess(updatedAction.getLegalProcess());

            Action editedAction = actionService.save(existingAction);

            URI location = new URI("/actions/" + editedAction.getId());
            return ResponseEntity.ok().location(location).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            Action toRemoveAction = actionService.getById(id);
            actionService.remove(toRemoveAction);
            return ResponseEntity.ok(toRemoveAction);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            Action action = actionService.getById(id);
            return ResponseEntity.ok(action);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
