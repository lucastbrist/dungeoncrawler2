package com.ltb.dungeoncrawler2.controllers;

import com.ltb.dungeoncrawler2.models.dto.UserResponse;
import com.ltb.dungeoncrawler2.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser() {
        return ResponseEntity.status(201).body(userService.createUser());
    }
}