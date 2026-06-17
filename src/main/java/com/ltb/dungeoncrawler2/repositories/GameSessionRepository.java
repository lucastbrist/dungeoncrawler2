package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    Optional<GameSession> findByUserId(Long userId);
}