package com.ltb.dungeoncrawler2.models.dto;

import com.ltb.dungeoncrawler2.enums.CharacterStatus;
import com.ltb.dungeoncrawler2.enums.ClassType;

import java.util.List;

public record CharacterResponse(
        Long id,
        String name,
        int level,
        int xpBanked,
        int currentDay,
        CharacterStatus status,
        SpeciesInfo species,
        ClassType classType,
        int leveledStrength,
        int leveledSense,
        int leveledSpeed,
        int totalStrength,
        int totalSense,
        int totalSpeed,
        int health,
        int damage,
        int spellDamage,
        int armorRating,
        int critChance,
        int stamina,
        int stealth,
        int pointsAtNextLevelUp,
        List<AbilityInfo> abilities
) {}
