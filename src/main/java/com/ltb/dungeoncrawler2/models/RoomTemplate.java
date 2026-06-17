package com.ltb.dungeoncrawler2.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_templates")
@Getter @NoArgsConstructor
public class RoomTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "min_width")
    private Integer minWidth;

    @Column(name = "max_width")
    private Integer maxWidth;

    @Column(name = "min_length")
    private Integer minLength;

    @Column(name = "max_length")
    private Integer maxLength;
}