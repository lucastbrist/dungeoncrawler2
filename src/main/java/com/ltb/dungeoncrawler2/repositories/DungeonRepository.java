package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.Dungeon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DungeonRepository extends JpaRepository<Dungeon, Long> {
}