package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {

    @Query("SELECT DISTINCT pc FROM PlayerCharacter pc JOIN FETCH pc.species LEFT JOIN FETCH pc.abilities ca LEFT JOIN FETCH ca.ability WHERE pc.id = :id")
    Optional<PlayerCharacter> findByIdWithSpeciesAndAbilities(@Param("id") Long id);
}