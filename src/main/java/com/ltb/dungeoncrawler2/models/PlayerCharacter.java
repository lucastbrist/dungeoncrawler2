package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.CharacterStatus;
import com.ltb.dungeoncrawler2.enums.ClassType;
import com.ltb.dungeoncrawler2.enums.GameMode;
import com.ltb.dungeoncrawler2.enums.SkillType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player_characters")
@Getter @Setter @NoArgsConstructor
public class PlayerCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", nullable = false)
    private Species species;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_type", nullable = false)
    private ClassType classType;

    @Column(nullable = false)
    private int level = 1;

    @Column(name = "xp_banked", nullable = false)
    private int xpBanked;

    @Column(name = "leveled_strength", nullable = false)
    private int leveledStrength;

    @Column(name = "leveled_sense", nullable = false)
    private int leveledSense;

    @Column(name = "leveled_speed", nullable = false)
    private int leveledSpeed;

    @OneToMany(mappedBy = "playerCharacter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacterAbility> abilities = new ArrayList<>();

    @Column(name = "current_day", nullable = false)
    private int currentDay = 1;

    @Column(name = "debt_attempts", nullable = false)
    private int debtAttempts;

    @Column(name = "debt_window_start")
    private Integer debtWindowStart;

    @Column(name = "beg_attempts", nullable = false)
    private int begAttempts;

    @Column(name = "beg_window_start")
    private Integer begWindowStart;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CharacterStatus status = CharacterStatus.ALIVE;

    @Column(name = "playthrough_seed")
    private Long playthroughSeed;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false)
    private GameMode mode;

    @Enumerated(EnumType.STRING)
    @Column(name = "discounted_skill", nullable = false)
    private SkillType discountedSkill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subspecies_id")
    private Subspecies subspecies;
}
