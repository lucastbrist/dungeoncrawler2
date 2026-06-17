CREATE TABLE themes (
    id                   BIGSERIAL    PRIMARY KEY,
    name                 VARCHAR(100) NOT NULL UNIQUE,
    description          TEXT,
    is_active            BOOLEAN      NOT NULL DEFAULT true,
    default_light_level  VARCHAR(20) NOT NULL DEFAULT 'AMBIENT' CHECK (default_light_level IN ('CRUSHING', 'DARK', 'AMBIENT', 'BRIGHT', 'BLINDING')),
    light_level_variance JSONB
);

CREATE TABLE room_templates (
    id          BIGSERIAL    PRIMARY KEY,
    theme_id    BIGINT       NOT NULL REFERENCES themes(id),
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    min_width   INT,
    max_width   INT,
    min_length  INT,
    max_length  INT
);

INSERT INTO themes (name) VALUES ('Generic');
INSERT INTO room_templates (theme_id, name)
SELECT id, 'Empty Room' FROM themes WHERE name = 'Generic';