package com.ltb.dungeoncrawler2.models;

public record CharacterStats(
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
