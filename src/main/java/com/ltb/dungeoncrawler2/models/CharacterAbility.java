package com.ltb.dungeoncrawler2.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "character_abilities")
@Getter @Setter @NoArgsConstructor
public class CharacterAbility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_character_id", nullable = false)
    private PlayerCharacter playerCharacter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ability_id", nullable = false)
    private Ability ability;

    @Column(name = "uses_remaining", nullable = false)
    private int usesRemaining;
}
