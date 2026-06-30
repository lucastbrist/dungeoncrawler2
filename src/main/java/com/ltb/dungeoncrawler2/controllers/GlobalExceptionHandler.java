package com.ltb.dungeoncrawler2.controllers;

import com.ltb.dungeoncrawler2.exceptions.BadRequestException;
import com.ltb.dungeoncrawler2.exceptions.ConflictException;
import com.ltb.dungeoncrawler2.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFound(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Void> handleConflict(ConflictException e) {
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}