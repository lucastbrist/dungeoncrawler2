CREATE TABLE player_characters (
    id                BIGSERIAL    PRIMARY KEY,
    user_id           BIGINT       NOT NULL REFERENCES users(id),
    name              VARCHAR(100) NOT NULL,
    species_id        BIGINT       NOT NULL REFERENCES species(id),
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
    playthrough_seed  BIGINT,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE species_abilities (
    species_id BIGINT NOT NULL REFERENCES species(id),
    ability_id BIGINT NOT NULL REFERENCES abilities(id),
    PRIMARY KEY (species_id, ability_id)
);

-- Shamble start with a Death Token; other species abilities seeded when designed
INSERT INTO species_abilities (species_id, ability_id)
SELECT s.id, a.id FROM species s, abilities a
WHERE s.name = 'Shamble' AND a.name = 'Death Token';

CREATE TABLE character_abilities (
    id                  BIGSERIAL PRIMARY KEY,
    player_character_id BIGINT    NOT NULL REFERENCES player_characters(id),
    ability_id          BIGINT    NOT NULL REFERENCES abilities(id),
    uses_remaining      INT       NOT NULL,
    UNIQUE (player_character_id, ability_id)
);
