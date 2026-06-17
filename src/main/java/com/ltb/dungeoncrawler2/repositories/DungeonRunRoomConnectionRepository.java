package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.DungeonRunRoomConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DungeonRunRoomConnectionRepository extends JpaRepository<DungeonRunRoomConnection, Long> {

    Optional<DungeonRunRoomConnection> findByDungeonRunIdAndConnectionId(Long dungeonRunId, Long connectionId);

    // Returns connections traversable from the given room: all where it is fromRoom, plus BIDIRECTIONAL where it is toRoom
    @Query("""
        SELECT rc FROM DungeonRunRoomConnection rc
        WHERE rc.dungeonRun.id = :runId
          AND (rc.connection.fromRoom.id = :roomId
               OR (rc.connection.toRoom.id = :roomId
                   AND rc.connection.template.traversalType = com.ltb.dungeoncrawler2.enums.TraversalType.BIDIRECTIONAL))
        """)
    List<DungeonRunRoomConnection> findByDungeonRunIdAndConnectionRoom(
            @Param("runId") Long dungeonRunId,
            @Param("roomId") Long roomId);
}