CREATE TABLE subspecies (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE player_characters (
    id                BIGSERIAL    PRIMARY KEY,
    user_id           BIGINT       NOT NULL REFERENCES users(id),
    name              VARCHAR(100) NOT NULL,
    species_id        BIGINT       NOT NULL REFERENCES species(id),
    subspecies_id     BIGINT       REFERENCES subspecies(id),
    class_type        VARCHAR(20)  NOT NULL,
    level             INT          NOT NULL DEFAULT 1,
    xp_banked         INT          NOT NULL DEFAULT 0,
    leveled_strength  INT          NOT NULL DEFAULT 0,
    leveled_sense     INT          NOT NULL DEFAULT 0,
    leveled_speed     INT          NOT NULL DEFAULT 0,
    current_day       INT          NOT NULL DEFAULT 1,
    debt_attempts     INT          NOT NULL DEFAULT 0,
    debt_window_start INT,
    beg_attempts      INT          NOT NULL DEFAULT 0,
    beg_window_start  INT,
    status            VARCHAR(20)  NOT NULL DEFAULT 'ALIVE',
    mode              VARCHAR(20)  NOT NULL,
    discounted_skill  VARCHAR(20)  NOT NULL,
    playthrough_seed  BIGINT
);

CREATE TABLE species_abilities (
    species_id BIGINT NOT NULL REFERENCES species(id),
    ability_id BIGINT NOT NULL REFERENCES abilities(id),
    PRIMARY KEY (species_id, ability_id)
);

-- Shamble: Soul Tether passive
INSERT INTO species_abilities (species_id, ability_id)
SELECT s.id, a.id FROM species s, abilities a
WHERE s.name = 'SHAMBLE' AND a.name = 'Soul Tether';

-- Attribute learning rate discounts by species
-- Human is excluded here: player chooses 2 from candidates seeded below
INSERT INTO species_abilities (species_id, ability_id)
SELECT s.id, a.id FROM species s, abilities a
WHERE s.name = 'DWARF'
  AND a.category = 'LEARNING_RATE'
  AND a.effect_config->>'targetType' = 'ATTRIBUTE'
  AND a.effect_config->>'target'     = 'STRENGTH'
  AND (a.effect_config->>'multiplier')::numeric = 2.0;

INSERT INTO species_abilities (species_id, ability_id)
SELECT s.id, a.id FROM species s, abilities a
WHERE s.name = 'ELF'
  AND a.category = 'LEARNING_RATE'
  AND a.effect_config->>'targetType' = 'ATTRIBUTE'
  AND a.effect_config->>'target'     = 'SENSE'
  AND (a.effect_config->>'multiplier')::numeric = 2.0;

INSERT INTO species_abilities (species_id, ability_id)
SELECT s.id, a.id FROM species s, abilities a
WHERE s.name = 'HALFLING'
  AND a.category = 'LEARNING_RATE'
  AND a.effect_config->>'targetType' = 'ATTRIBUTE'
  AND a.effect_config->>'target'     = 'SPEED'
  AND (a.effect_config->>'multiplier')::numeric = 2.0;

INSERT INTO species_abilities (species_id, ability_id)
SELECT s.id, a.id FROM species s, abilities a
WHERE s.name = 'SHAMBLE'
  AND a.category = 'LEARNING_RATE'
  AND a.effect_config->>'targetType' = 'ATTRIBUTE'
  AND (a.effect_config->>'multiplier')::numeric = 0.5;

-- Human: all three 2x attribute abilities are candidates; player chooses two at creation
INSERT INTO species_abilities (species_id, ability_id)
SELECT s.id, a.id FROM species s, abilities a
WHERE s.name = 'HUMAN'
  AND a.category = 'LEARNING_RATE'
  AND a.effect_config->>'targetType' = 'ATTRIBUTE'
  AND a.effect_config->>'target'     IN ('STRENGTH', 'SENSE', 'SPEED')
  AND (a.effect_config->>'multiplier')::numeric = 2.0;

CREATE TABLE character_abilities (
    id                  BIGSERIAL PRIMARY KEY,
    player_character_id BIGINT    NOT NULL REFERENCES player_characters(id),
    ability_id          BIGINT    NOT NULL REFERENCES abilities(id),
    uses_remaining      INT       NOT NULL,
    UNIQUE (player_character_id, ability_id)
);