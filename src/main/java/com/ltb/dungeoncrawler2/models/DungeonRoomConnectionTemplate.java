package com.ltb.dungeoncrawler2.models;

import com.ltb.dungeoncrawler2.enums.TraversalType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dungeon_room_connection_templates")
@Getter @NoArgsConstructor
public class DungeonRoomConnectionTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "traversal_type", nullable = false)
    private TraversalType traversalType;

    @Column(name = "reverse_condition")
    private String reverseCondition;

    @Column(name = "forward_narrative")
    private String forwardNarrative;

    @Column(name = "reverse_narrative")
    private String reverseNarrative;

    @Column(name = "locked_narrative")
    private String lockedNarrative;
}