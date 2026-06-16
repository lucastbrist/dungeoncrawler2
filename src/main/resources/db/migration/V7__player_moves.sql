CREATE TABLE player_moves (
    id             BIGSERIAL    PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    description    TEXT,
    attack_type    VARCHAR(20)  NOT NULL,
    damage_modifier INT         NOT NULL DEFAULT 0,
    armor_penetration DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    stamina_cost   INT          NOT NULL,
    is_base_move   BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE item_moves (
    item_id        BIGINT NOT NULL REFERENCES items(id),
    player_move_id BIGINT NOT NULL REFERENCES player_moves(id),
    PRIMARY KEY (item_id, player_move_id)
);

INSERT INTO player_moves (name, attack_type, stamina_cost, is_base_move) VALUES
    ('Punch',   'MELEE', 1, TRUE),
    ('Kick',    'MELEE', 1, TRUE),
    ('Cantrip', 'MAGIC', 1, TRUE);
