CREATE TABLE abilities (
    id            BIGSERIAL    PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    description   TEXT,
    initial_uses  INT          NOT NULL DEFAULT 1,
    category      VARCHAR(30)  NOT NULL DEFAULT 'PASSIVE'
                               CHECK (category IN ('PASSIVE', 'ACTIVE', 'INNATE_MOVE', 'LEARNING_RATE')),
    effect_config JSONB
);

INSERT INTO abilities (name, description, initial_uses, category) VALUES
    ('PLACEHOLDER_DEATH_TOKEN_NAME',  'PLACEHOLDER_DEATH_TOKEN_DESCRIPTION', 1,  'ACTIVE'),
    ('Soul Tether',  'PLACEHOLDER_SOUL_TETHER_DESCRIPTION', -1, 'PASSIVE');

-- Attribute learning rate abilities (x2.0) — used by Elf, Dwarf, Halfling, and Human (chosen at creation)
INSERT INTO abilities (name, initial_uses, category, effect_config) VALUES
    ('LR_ATTR_STRENGTH_2X', -1, 'LEARNING_RATE', '{"targetType": "ATTRIBUTE", "target": "STRENGTH", "multiplier": 2.0}'),
    ('LR_ATTR_SENSE_2X',    -1, 'LEARNING_RATE', '{"targetType": "ATTRIBUTE", "target": "SENSE",    "multiplier": 2.0}'),
    ('LR_ATTR_SPEED_2X',    -1, 'LEARNING_RATE', '{"targetType": "ATTRIBUTE", "target": "SPEED",    "multiplier": 2.0}');

-- Attribute learning rate abilities (x0.5) — Shamble
INSERT INTO abilities (name, initial_uses, category, effect_config) VALUES
    ('LR_ATTR_STRENGTH_0_5X', -1, 'LEARNING_RATE', '{"targetType": "ATTRIBUTE", "target": "STRENGTH", "multiplier": 0.5}'),
    ('LR_ATTR_SENSE_0_5X',    -1, 'LEARNING_RATE', '{"targetType": "ATTRIBUTE", "target": "SENSE",    "multiplier": 0.5}'),
    ('LR_ATTR_SPEED_0_5X',    -1, 'LEARNING_RATE', '{"targetType": "ATTRIBUTE", "target": "SPEED",    "multiplier": 0.5}');