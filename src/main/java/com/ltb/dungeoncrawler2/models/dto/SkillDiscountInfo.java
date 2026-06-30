package com.ltb.dungeoncrawler2.models.dto;

import com.ltb.dungeoncrawler2.enums.SkillType;

public record SkillDiscountInfo(SkillType target, float multiplier) {}