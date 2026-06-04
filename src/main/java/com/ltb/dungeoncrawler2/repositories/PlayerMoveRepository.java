package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.PlayerMove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerMoveRepository extends JpaRepository<PlayerMove, Long> {

    List<PlayerMove> findByBaseMoveTrue();
}
