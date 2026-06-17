package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.RoomTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTemplateRepository extends JpaRepository<RoomTemplate, Long> {
    Optional<RoomTemplate> findByNameAndThemeId(String name, Long themeId);
}