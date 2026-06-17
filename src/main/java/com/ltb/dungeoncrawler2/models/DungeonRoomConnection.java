package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.Direction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dungeon_room_connections")
@Getter @Setter @NoArgsConstructor
public class DungeonRoomConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dungeon_id", nullable = false)
    private Dungeon dungeon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_room_id", nullable = false)
    private DungeonRoom fromRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_room_id", nullable = false)
    private DungeonRoom toRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private DungeonRoomConnectionTemplate template;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Column(name = "from_cell_x")
    private Integer fromCellX;

    @Column(name = "from_cell_y")
    private Integer fromCellY;

    @Column(name = "to_cell_x")
    private Integer toCellX;

    @Column(name = "to_cell_y")
    private Integer toCellY;
}