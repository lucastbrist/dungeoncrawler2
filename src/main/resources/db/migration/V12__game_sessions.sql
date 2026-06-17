CREATE TABLE game_sessions (
    id                     BIGSERIAL   PRIMARY KEY,
    user_id                BIGINT      NOT NULL REFERENCES users(id),
    player_character_id    BIGINT      NOT NULL REFERENCES player_characters(id),
    current_location       VARCHAR(20) NOT NULL DEFAULT 'HUB',
    current_dungeon_run_id BIGINT      REFERENCES dungeon_runs(id),
    combat_state           JSONB
);

CREATE UNIQUE INDEX game_sessions_user_unique ON game_sessions(user_id);