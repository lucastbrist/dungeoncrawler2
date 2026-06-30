package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.AbilityCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "abilities")
@Getter @NoArgsConstructor
public class Ability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "initial_uses", nullable = false)
    private int initialUses;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbilityCategory category;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "effect_config")
    private Map<String, Object> effectConfig;
}