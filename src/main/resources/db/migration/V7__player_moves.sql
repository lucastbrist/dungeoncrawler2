CREATE TABLE player_moves (
    id                BIGSERIAL        PRIMARY KEY,
    name              VARCHAR(100)     NOT NULL,
    description       TEXT,
    attack_type       VARCHAR(20)      NOT NULL,
    damage_modifier   INT              NOT NULL DEFAULT 0,
    armor_penetration DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    stamina_cost      INT              NOT NULL DEFAULT 0,
    arcana_cost       INT              NOT NULL DEFAULT 0,
    ammo_consumed     INT              NOT NULL DEFAULT 0,
    combat_action_type         VARCHAR(10)      CHECK (combat_action_type IN ('MAIN', 'SWIFT', 'MOVE', 'REACTION')),
    materia_consumed  BOOLEAN          NOT NULL DEFAULT FALSE,
    is_base_move      BOOLEAN          NOT NULL DEFAULT FALSE
);

CREATE TABLE item_moves (
    item_id        BIGINT NOT NULL REFERENCES items(id),
    player_move_id BIGINT NOT NULL REFERENCES player_moves(id),
    PRIMARY KEY (item_id, player_move_id)
);

INSERT INTO player_moves (name, attack_type, stamina_cost, combat_action_type, is_base_move) VALUES
    ('Punch', 'MELEE', 1, 'MAIN', TRUE),
    ('Kick',  'MELEE', 1, 'MAIN', TRUE);

INSERT INTO player_moves (name, attack_type, arcana_cost, combat_action_type, is_base_move) VALUES
    ('Cantrip', 'MAGIC', 1, 'MAIN', TRUE);