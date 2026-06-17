CREATE TABLE dungeons (
    id              BIGSERIAL    PRIMARY KEY,
    theme_id        BIGINT       NOT NULL REFERENCES themes(id),
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    generation_seed BIGINT
);

CREATE TABLE dungeon_rooms (
    id               BIGSERIAL PRIMARY KEY,
    dungeon_id       BIGINT    NOT NULL REFERENCES dungeons(id),
    room_template_id BIGINT    NOT NULL REFERENCES room_templates(id),
    is_boss_room     BOOLEAN   NOT NULL DEFAULT false,
    grid_x           INT,
    grid_y           INT,
    grid_z           INT,
    width            INT       NOT NULL DEFAULT 1,
    length           INT       NOT NULL DEFAULT 1
);

CREATE TABLE dungeon_room_connection_templates (
    id                BIGSERIAL   PRIMARY KEY,
    traversal_type    VARCHAR(30) NOT NULL,
    reverse_condition VARCHAR(100),
    forward_narrative TEXT,
    reverse_narrative TEXT,
    locked_narrative  TEXT,
    CONSTRAINT chk_conditional_has_condition
        CHECK (traversal_type != 'ONE_WAY_CONDITIONAL' OR reverse_condition IS NOT NULL)
);

CREATE TABLE dungeon_room_connections (
    id           BIGSERIAL   PRIMARY KEY,
    dungeon_id   BIGINT      NOT NULL REFERENCES dungeons(id),
    from_room_id BIGINT      NOT NULL REFERENCES dungeon_rooms(id),
    to_room_id   BIGINT      NOT NULL REFERENCES dungeon_rooms(id),
    template_id  BIGINT      NOT NULL REFERENCES dungeon_room_connection_templates(id),
    direction    VARCHAR(10) NOT NULL,
    from_cell_x  INT,
    from_cell_y  INT,
    to_cell_x    INT,
    to_cell_y    INT
);

-- Stub BIDIRECTIONAL template — used by Phase 2 generation
INSERT INTO dungeon_room_connection_templates (traversal_type) VALUES ('BIDIRECTIONAL');