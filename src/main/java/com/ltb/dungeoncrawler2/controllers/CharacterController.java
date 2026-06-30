package com.ltb.dungeoncrawler2.controllers;

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
        CharacterResponse response = request.speciesId() != null
                ? characterService.createCharacter(request.userId(), request.name(), request.speciesId(),
                        request.classType(), request.mode(), request.discountedSkill(), request.discountedAttributes())
                : characterService.createCharacter(request.userId(), request.name(), request.speciesName(),
                        request.classType(), request.mode(), request.discountedSkill(), request.discountedAttributes());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getCharacter(@PathVariable Long id) {
        return characterService.getCharacter(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}