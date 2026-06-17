package com.ltb.dungeoncrawler2.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dungeon_rooms")
@Getter @Setter @NoArgsConstructor
public class DungeonRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dungeon_id", nullable = false)
    private Dungeon dungeon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_template_id", nullable = false)
    private RoomTemplate roomTemplate;

    @Column(name = "is_boss_room", nullable = false)
    private boolean isBossRoom = false;

    @Column(name = "grid_x")
    private Integer gridX;

    @Column(name = "grid_y")
    private Integer gridY;

    @Column(name = "grid_z")
    private Integer gridZ;

    @Column(name = "width", nullable = false)
    private int width = 1;

    @Column(name = "length", nullable = false)
    private int length = 1;
}