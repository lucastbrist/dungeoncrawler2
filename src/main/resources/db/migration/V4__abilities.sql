CREATE TABLE abilities (
    id            BIGSERIAL    PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    description   TEXT,
    initial_uses  INT          NOT NULL DEFAULT 1
);

INSERT INTO abilities (name, description, initial_uses) VALUES
    ('Death Token', 'When you would die, choose to survive with 1 HP instead. Consumed on use.', 1);
