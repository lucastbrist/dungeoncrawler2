ALTER TABLE game_sessions ADD CONSTRAINT chk_current_location
    CHECK (current_location IN ('HUB', 'DUNGEON'));

ALTER TABLE dungeon_runs ADD CONSTRAINT chk_run_status
    CHECK (status IN ('ACTIVE', 'INACTIVE', 'ABANDONED'));

ALTER TABLE dungeon_run_rooms ADD CONSTRAINT chk_room_status
    CHECK (status IN ('UNVISITED', 'VISITED'));

ALTER TABLE dungeon_room_connections ADD CONSTRAINT chk_direction
    CHECK (direction IN ('NORTH', 'SOUTH', 'EAST', 'WEST', 'UP', 'DOWN'));

ALTER TABLE dungeon_room_connection_templates ADD CONSTRAINT chk_traversal_type
    CHECK (traversal_type IN ('BIDIRECTIONAL', 'ONE_WAY_PERMANENT', 'ONE_WAY_CONDITIONAL', 'ONE_WAY_UNLOCKABLE'));