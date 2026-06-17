package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.enums.Direction;
import com.ltb.dungeoncrawler2.models.DungeonRoomConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DungeonRoomConnectionRepository extends JpaRepository<DungeonRoomConnection, Long> {

    List<DungeonRoomConnection> findByDungeonIdAndFromRoomId(Long dungeonId, Long fromRoomId);

    List<DungeonRoomConnection> findByDungeonIdAndToRoomId(Long dungeonId, Long toRoomId);

    Optional<DungeonRoomConnection> findByDungeonIdAndFromRoomIdAndDirection(Long dungeonId, Long fromRoomId, Direction direction);

    Optional<DungeonRoomConnection> findByDungeonIdAndToRoomIdAndDirection(Long dungeonId, Long toRoomId, Direction direction);
}