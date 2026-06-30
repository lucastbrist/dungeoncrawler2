ALTER TABLE player_characters
    ADD CONSTRAINT chk_status          CHECK (status          IN ('ALIVE', 'DEAD', 'RETIRED')),
    ADD CONSTRAINT chk_class_type      CHECK (class_type      IN ('WARRIOR', 'SORCERER', 'BURGLAR')),
    ADD CONSTRAINT chk_mode            CHECK (mode            IN ('ENDLESS', 'LEGACY', 'CAMPAIGN', 'IRONMAN', 'DUNGEON', 'MONSTER')),
    ADD CONSTRAINT chk_discounted_skill CHECK (discounted_skill IN (
        'MELEE', 'MISSILES', 'ARMOR',
        'PROJECTION', 'PERCEPTION', 'INFUSION',
        'SUBTERFUGE', 'EVASION', 'CONDITIONING'
    ));