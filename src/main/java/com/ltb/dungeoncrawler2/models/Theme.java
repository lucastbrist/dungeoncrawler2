package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.LightLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "themes")
@Getter @NoArgsConstructor
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_light_level", nullable = false)
    private LightLevel defaultLightLevel = LightLevel.AMBIENT;

    @Column(name = "light_level_variance", columnDefinition = "jsonb")
    private String lightLevelVariance;
}