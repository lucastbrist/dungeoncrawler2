package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.SpeciesName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "species")
@Getter @NoArgsConstructor
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpeciesName name;

    @Column
    private String description;

    @Column(name = "strength_mod", nullable = false)
    private int strengthMod;

    @Column(name = "sense_mod", nullable = false)
    private int senseMod;

    @Column(name = "speed_mod", nullable = false)
    private int speedMod;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "species_abilities",
        joinColumns = @JoinColumn(name = "species_id"),
        inverseJoinColumns = @JoinColumn(name = "ability_id")
    )
    private List<Ability> abilities = new ArrayList<>();
}
