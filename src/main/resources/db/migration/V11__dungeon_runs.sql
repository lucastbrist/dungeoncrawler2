CREATE TABLE dungeon_runs (
    id                  BIGSERIAL   PRIMARY KEY,
    player_character_id BIGINT      NOT NULL REFERENCES player_characters(id),
    dungeon_id          BIGINT      NOT NULL REFERENCES dungeons(id),
    current_room_id     BIGINT      REFERENCES dungeon_rooms(id),
    status              VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE dungeon_run_rooms (
    id               BIGSERIAL   PRIMARY KEY,
    dungeon_run_id   BIGINT      NOT NULL REFERENCES dungeon_runs(id),
    room_id          BIGINT      NOT NULL REFERENCES dungeon_rooms(id),
    status           VARCHAR(20) NOT NULL DEFAULT 'UNVISITED',
    completed_checks JSONB,
    UNIQUE (dungeon_run_id, room_id)
);

CREATE TABLE dungeon_run_room_connections (
    id             BIGSERIAL PRIMARY KEY,
    dungeon_run_id BIGINT    NOT NULL REFERENCES dungeon_runs(id),
    connection_id  BIGINT    NOT NULL REFERENCES dungeon_room_connections(id),
    is_reversed    BOOLEAN   NOT NULL DEFAULT false,
    is_discovered  BOOLEAN   NOT NULL DEFAULT false,
    UNIQUE (dungeon_run_id, connection_id)
);