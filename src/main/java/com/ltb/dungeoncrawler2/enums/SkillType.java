package com.ltb.dungeoncrawler2.enums;

public enum SkillType {
    // Strength skills
    MELEE,          // roll-based: to-hit for melee weapons
    MISSILES,       // roll-based: to-hit for ranged weapons and thrown weapons
    ARMOR,          // efficacy-based: amplifies equipped armor's DR

    // Sense skills
    PROJECTION,     // roll-based: to-hit for offensive/debuff spells
    PERCEPTION,     // roll-based: all perception checks
    INFUSION,       // efficacy-based: heal and buff spell output multiplier

    // Speed skills
    SUBTERFUGE,     // roll-based: stealth, lockpicking, trap disarm
    EVASION,        // roll-based: feeds dodge roll; penalized by equipped+belted weight
    CONDITIONING    // efficacy-based: reduces weight penalties (dodge + stamina)
}