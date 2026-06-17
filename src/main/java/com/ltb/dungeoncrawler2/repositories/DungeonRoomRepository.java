package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.DungeonRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DungeonRoomRepository extends JpaRepository<DungeonRoom, Long> {
}