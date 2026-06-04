package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpeciesRepository extends JpaRepository<Species, Long> {

    // DISTINCT prevents duplicate Species rows when a species has multiple abilities (one row per join)
    
    @Query("SELECT DISTINCT s FROM Species s LEFT JOIN FETCH s.abilities WHERE s.id = :id")
    Optional<Species> findByIdWithAbilities(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM Species s LEFT JOIN FETCH s.abilities WHERE LOWER(s.name) = LOWER(:name)")
    Optional<Species> findByNameWithAbilities(@Param("name") String name);
}
