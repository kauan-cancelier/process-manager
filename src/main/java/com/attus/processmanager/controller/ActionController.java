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
import java.net.URISyntaxException;

@RestController
@RequestMapping("actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @GetMapping
    public ResponseEntity<Object> list(@RequestParam(required = false) String type) {
        try {
            if (type != null && !type.isBlank()) {
                return ResponseEntity.ok(actionService.list(ActionType.tryConvert(type)));
            }
            return ResponseEntity.ok(actionService.list(null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ActionSaveRequest actionSaveRequest) {
        try {
            Action action = actionService.save(actionSaveRequest.toModel());
            URI location = new URI("/actions/" + action.getId());
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody ActionUpdateRequest updatedAction) {
        try {
            Action existingAction = actionService.getById(id);

            existingAction.setDescription(updatedAction.getDescription());
            if (updatedAction.getType() != null) {
                existingAction.setType(ActionType.tryConvert(updatedAction.getType()));
            }
            existingAction.setLegalProcess(updatedAction.getLegalProcess());

            Action editedAction = actionService.save(existingAction);

            URI location = new URI("/actions/" + editedAction.getId());
            return ResponseEntity.ok().location(location).build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        try {
            Action toRemoveAction = actionService.getById(id);
            actionService.remove(toRemoveAction);
            return ResponseEntity.ok(toRemoveAction);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        try {
            Action action = actionService.getById(id);
            return ResponseEntity.ok(action);
        }catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
