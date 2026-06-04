CREATE TABLE damage_types (
    id       BIGSERIAL   PRIMARY KEY,
    name     VARCHAR(50) NOT NULL,
    category VARCHAR(10) NOT NULL
);

CREATE TABLE item_types (
    id             BIGSERIAL   PRIMARY KEY,
    name           VARCHAR(50) NOT NULL,
    max_stack_size INT
);

INSERT INTO damage_types (name, category) VALUES
    ('Slashing',    'PHYSICAL'),
    ('Piercing',    'PHYSICAL'),
    ('Bludgeoning', 'PHYSICAL'),
    ('Fire',        'MAGICAL'),
    ('Ice',         'MAGICAL'),
    ('Lightning',   'MAGICAL'),
    ('Poison',      'PHYSICAL');

INSERT INTO item_types (name, max_stack_size) VALUES
    ('Weapon',  NULL),
    ('Armor',   NULL),
    ('Trinket', NULL),
    ('Tool',    NULL),
    ('Potion',  5),
    ('Gold',    NULL);
