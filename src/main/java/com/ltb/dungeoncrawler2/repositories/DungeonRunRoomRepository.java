package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.DungeonRunRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DungeonRunRoomRepository extends JpaRepository<DungeonRunRoom, Long> {

    Optional<DungeonRunRoom> findByDungeonRunIdAndRoomId(Long dungeonRunId, Long roomId);

    List<DungeonRunRoom> findByDungeonRunId(Long dungeonRunId);
}