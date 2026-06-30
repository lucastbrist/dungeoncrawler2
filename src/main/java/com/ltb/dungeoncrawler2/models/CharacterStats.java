package com.ltb.dungeoncrawler2.models;

public record CharacterStats(
        int   totalStrength,
        int   totalSense,
        int   totalSpeed,
        int   health,
        int   arcana,
        int   stamina,
        int   physicalDamage,       // Strength + Speed × SHARED_SCALAR, or only one
        int   magicDamage,          // Sense × ATTRIBUTE_SECOND_SCALAR_MULT
        float armorDR,
        int   armorDT,          
        int   critThreshold,        // natural roll at or above this crits; natural 100 always crits
        float critDamageMultiplier,
        int   carryWeight,
        int   initiative            // base value; d100 roll added at combat resolution
) {}