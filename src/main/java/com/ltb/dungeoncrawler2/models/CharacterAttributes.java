package com.ltb.dungeoncrawler2.models;

public record CharacterAttributes(
        int totalStrength,
        int totalSense,
        int totalSpeed,
        int health,
        int damage,
        int spellDamage,
        int armorRating,
        int critChance,
        int stamina,
        int stealth
) {}
