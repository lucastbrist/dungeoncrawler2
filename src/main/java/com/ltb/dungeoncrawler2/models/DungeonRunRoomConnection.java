package com.ltb.dungeoncrawler2.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dungeon_run_room_connections")
@Getter @Setter @NoArgsConstructor
public class DungeonRunRoomConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dungeon_run_id", nullable = false)
    private DungeonRun dungeonRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connection_id", nullable = false)
    private DungeonRoomConnection connection;

    @Column(name = "is_reversed", nullable = false)
    private boolean isReversed = false;

    @Column(name = "is_discovered", nullable = false)
    private boolean isDiscovered = false;
}