ALTER TABLE player_characters
    ADD CONSTRAINT chk_status     CHECK (status     IN ('ALIVE', 'DEAD', 'RETIRED')),
    ADD CONSTRAINT chk_class_type CHECK (class_type IN ('WARRIOR', 'SORCERER', 'THIEF'));
