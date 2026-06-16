CREATE TABLE species (
    id           BIGSERIAL    PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    strength_mod INT          NOT NULL DEFAULT 0,
    sense_mod    INT          NOT NULL DEFAULT 0,
    speed_mod    INT          NOT NULL DEFAULT 0
);

INSERT INTO species (name, strength_mod, sense_mod, speed_mod) VALUES
    ('Human',     1,  1,  1),
    ('Elf',       0,  2,  1),
    ('Dwarf',     3,  0,  0),
    ('Halfling', -3,  0,  3),
    ('Gnome',    -3,  3,  0),
    ('Shamble',  -3, -3, -3),
    ('Beastfolk', 2, -1,  2);