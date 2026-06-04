package com.ltb.dungeoncrawler2.models.dto;

import com.ltb.dungeoncrawler2.enums.ClassType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCharacterRequest(
        @NotNull Long userId,
        @NotBlank String name,
        Long speciesId,
        String speciesName,
        @NotNull ClassType classType
) {
    @AssertTrue(message = "Must provide speciesId or speciesName")
    boolean isSpeciesProvided() {
        return speciesId != null || (speciesName != null && !speciesName.isBlank());
    }
}
