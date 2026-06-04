package com.ltb.dungeoncrawler2.controllers;

import com.ltb.dungeoncrawler2.exceptions.NotFoundException;
import com.ltb.dungeoncrawler2.models.dto.CharacterResponse;
import com.ltb.dungeoncrawler2.models.dto.CreateCharacterRequest;
import com.ltb.dungeoncrawler2.services.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping
    public ResponseEntity<CharacterResponse> createCharacter(@Valid @RequestBody CreateCharacterRequest request) {
        try {
            CharacterResponse response = request.speciesId() != null
                    ? characterService.createCharacter(request.userId(), request.name(), request.speciesId(), request.classType())
                    : characterService.createCharacter(request.userId(), request.name(), request.speciesName(), request.classType());
            return ResponseEntity.status(201).body(response);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getCharacter(@PathVariable Long id) {
        return characterService.getCharacter(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
