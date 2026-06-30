CREATE TABLE species (
    id           BIGSERIAL    PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    strength_mod INT          NOT NULL DEFAULT 0,
    sense_mod    INT          NOT NULL DEFAULT 0,
    speed_mod    INT          NOT NULL DEFAULT 0
);

INSERT INTO species (name, strength_mod, sense_mod, speed_mod) VALUES
    ('HUMAN',     1,  1,  1),
    ('ELF',       0,  2,  1),
    ('DWARF',     3,  0,  0),
    ('GNOME',    -3,  3,  0),
    ('HALFLING', -3,  0,  3),
    ('BEASTFOLK', 2, -1,  2),
    ('SHAMBLE',  -3, -3, -3);