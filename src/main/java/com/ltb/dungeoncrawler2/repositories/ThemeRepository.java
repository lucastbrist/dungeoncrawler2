package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Optional<Theme> findByName(String name);
}