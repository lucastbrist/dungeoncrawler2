package com.ltb.dungeoncrawler2.models.dto;

import com.ltb.dungeoncrawler2.enums.SpeciesName;

public record SpeciesInfo(Long id, SpeciesName name, String description) {}
