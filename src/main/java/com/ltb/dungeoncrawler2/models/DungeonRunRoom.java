package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dungeon_run_rooms")
@Getter @Setter @NoArgsConstructor
public class DungeonRunRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dungeon_run_id", nullable = false)
    private DungeonRun dungeonRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private DungeonRoom room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.UNVISITED;

    @Column(name = "completed_checks", columnDefinition = "jsonb")
    private String completedChecks;
}