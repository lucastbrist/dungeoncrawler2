package com.ltb.dungeoncrawler2.models.dto;

import com.ltb.dungeoncrawler2.enums.AttributeType;
import com.ltb.dungeoncrawler2.enums.ClassType;
import com.ltb.dungeoncrawler2.enums.GameMode;
import com.ltb.dungeoncrawler2.enums.SkillType;
import com.ltb.dungeoncrawler2.enums.SpeciesName;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateCharacterRequest(
        @NotNull Long userId,
        @NotBlank String name,
        Long speciesId,
        SpeciesName speciesName,
        @NotNull ClassType classType,
        @NotNull GameMode mode,
        @NotNull SkillType discountedSkill,
        List<AttributeType> discountedAttributes
) {
    @AssertTrue(message = "Must provide speciesId or speciesName")
    boolean isSpeciesProvided() {
        return speciesId != null || speciesName != null;
    }
}