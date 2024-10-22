package com.attus.processmanager.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleForeignKeyViolation(DataIntegrityViolationException ex) {
        log.error("DataIntegrityVilationException caught: {}",ex.getMessage());
        return ResponseEntity.badRequest().body("Unable to delete as there are records related to this entity. ");
    }

}
