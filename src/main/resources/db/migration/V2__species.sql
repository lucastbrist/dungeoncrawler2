CREATE TABLE species (
    id           BIGSERIAL    PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    strength_mod INT          NOT NULL DEFAULT 0,
    sense_mod    INT          NOT NULL DEFAULT 0,
    speed_mod    INT          NOT NULL DEFAULT 0
);

INSERT INTO species (name, strength_mod, sense_mod, speed_mod) VALUES
    ('Human',    1,  1,  1),
    ('Elf',     -1,  2,  2),
    ('Dwarf',    3, -2,  0),
    ('Halfling', -2,  0,  3),
    ('Gnome',   -3,  3,  1),
    ('Shamble', -1, -1, -1);
