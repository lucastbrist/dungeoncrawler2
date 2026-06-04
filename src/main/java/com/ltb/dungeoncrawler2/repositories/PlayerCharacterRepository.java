package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {
}
