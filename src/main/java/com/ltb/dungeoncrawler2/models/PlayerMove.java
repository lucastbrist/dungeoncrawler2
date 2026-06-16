package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.AttackType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_moves")
@Getter @NoArgsConstructor
public class PlayerMove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "attack_type", nullable = false)
    private AttackType attackType;

    @Column(name = "damage_modifier", nullable = false)
    private int damageModifier;

    @Column(name = "armor_penetration", nullable = false)
    private double armorPenetration;

    @Column(name = "stamina_cost", nullable = false)
    private int staminaCost;

    @Column(name = "is_base_move", nullable = false)
    private boolean baseMove;
}
