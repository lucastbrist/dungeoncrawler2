package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.LocationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_sessions")
@Getter @Setter @NoArgsConstructor
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_character_id", nullable = false)
    private PlayerCharacter playerCharacter;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_location", nullable = false)
    private LocationType currentLocation = LocationType.HUB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_dungeon_run_id")
    private DungeonRun currentDungeonRun;

    @Column(name = "combat_state", columnDefinition = "jsonb")
    private String combatState;
}