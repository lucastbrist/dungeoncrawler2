package com.ltb.dungeoncrawler2.models.dto;

import com.ltb.dungeoncrawler2.enums.AbilityCategory;

public record AbilityInfo(
        Long id,
        String name,
        int usesRemaining,
        AbilityCategory category
) {}