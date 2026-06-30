package com.ltb.dungeoncrawler2.repositories;

import com.ltb.dungeoncrawler2.models.Ability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AbilityRepository extends JpaRepository<Ability, Long> {

    Optional<Ability> findByName(String name);
}
