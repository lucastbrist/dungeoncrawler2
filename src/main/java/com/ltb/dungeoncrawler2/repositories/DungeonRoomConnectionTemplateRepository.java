package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.enums.TraversalType;
import com.ltb.dungeoncrawler2.models.DungeonRoomConnectionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DungeonRoomConnectionTemplateRepository extends JpaRepository<DungeonRoomConnectionTemplate, Long> {
    Optional<DungeonRoomConnectionTemplate> findFirstByTraversalType(TraversalType traversalType);
}