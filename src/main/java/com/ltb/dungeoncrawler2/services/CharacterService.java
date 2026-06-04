package com.ltb.dungeoncrawler2.services;

import com.ltb.dungeoncrawler2.enums.ClassType;
import com.ltb.dungeoncrawler2.exceptions.NotFoundException;
import com.ltb.dungeoncrawler2.models.CharacterAbility;
import com.ltb.dungeoncrawler2.models.CharacterAttributes;
import com.ltb.dungeoncrawler2.models.PlayerCharacter;
import com.ltb.dungeoncrawler2.models.Species;
import com.ltb.dungeoncrawler2.models.dto.AbilityInfo;
import com.ltb.dungeoncrawler2.models.dto.CharacterResponse;
import com.ltb.dungeoncrawler2.models.dto.SpeciesInfo;
import com.ltb.dungeoncrawler2.repositories.PlayerCharacterRepository;
import com.ltb.dungeoncrawler2.repositories.SpeciesRepository;
import com.ltb.dungeoncrawler2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterService {

    // --- Named constants ---
    public static final int BASE_STRENGTH                = 10;
    public static final int BASE_SENSE                   = 10;
    public static final int BASE_SPEED                   = 10;
    public static final int BASE_HEALTH                 = 100;
    public static final int BASE_DAMAGE                 = 10;
    public static final int BASE_SPELL_DAMAGE           = 10;
    public static final int BASE_CRIT_CHANCE            = 10;
    public static final int LEVEL_UP_POINTS_MULTIPLIER  = 2;
    public static final int WARRIOR_HEALTH_BONUS        = 10;
    public static final int WARRIOR_DAMAGE_BONUS        = 3;
    public static final int SORCERER_SPELL_DAMAGE_BONUS = 6;
    public static final int CRIT_STAT_DIVISOR            = 10;
    public static final int STEALTH_SENSE_DIVISOR        = 2;
    public static final int HEALTH_STRENGTH_DIVISOR = 2;
    // Stamina constants live here until CombatService is built in Phase 3
    public static final int BASE_STAMINA                 = 3;
    public static final int SPEED_STAMINA_DIVISOR        = 30;

    private final UserRepository userRepo;
    private final SpeciesRepository speciesRepo;
    private final PlayerCharacterRepository characterRepo;

    // WithAbilities variants used here because createCharacter grants species abilities to the character.
    // Plain findById/findByName would leave species.abilities as an unloaded proxy outside the fetch boundary.
    // Overloaded to handle @Transactional and the either/or branches of id/name cleanly

    @Transactional
    public CharacterResponse createCharacter(Long userId, String name, Long speciesId, ClassType classType) {
        var species = speciesRepo.findByIdWithAbilities(speciesId)
                .orElseThrow(() -> new NotFoundException("Species not found: " + speciesId));
        return createCharacter(userId, name, species, classType);
    }

    @Transactional
    public CharacterResponse createCharacter(Long userId, String name, String speciesName, ClassType classType) {
        var species = speciesRepo.findByNameWithAbilities(speciesName)
                .orElseThrow(() -> new NotFoundException("Species not found: " + speciesName));
        return createCharacter(userId, name, species, classType);
    }

    private CharacterResponse createCharacter(Long userId, String name, Species species, ClassType classType) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        var pc = new PlayerCharacter();
        pc.setUser(user);
        pc.setName(name);
        pc.setSpecies(species);
        pc.setClassType(classType);

        for (var ability : species.getAbilities()) {
            var grant = new CharacterAbility();
            grant.setPlayerCharacter(pc);
            grant.setAbility(ability);
            grant.setUsesRemaining(ability.getInitialUses());
            pc.getAbilities().add(grant);
        }

        return toResponse(characterRepo.save(pc));
    }

    @Transactional(readOnly = true)
    public Optional<CharacterResponse> getCharacter(Long id) {
        return characterRepo.findById(id).map(this::toResponse);
    }

    private CharacterResponse toResponse(PlayerCharacter pc) {
        CharacterAttributes attributes = recalculateAttributes(pc);
        List<AbilityInfo> abilityList = pc.getAbilities().stream()
                .map(ca -> new AbilityInfo(
                        ca.getAbility().getId(),
                        ca.getAbility().getName(),
                        ca.getUsesRemaining()))
                .toList();
        return new CharacterResponse(
                pc.getId(),
                pc.getName(),
                pc.getLevel(),
                pc.getXpBanked(),
                pc.getCurrentDay(),
                pc.getStatus(),
                new SpeciesInfo(
                        pc.getSpecies().getId(),
                        pc.getSpecies().getName(),
                        pc.getSpecies().getDescription()),
                pc.getClassType(),
                pc.getLeveledStrength(),
                pc.getLeveledSense(),
                pc.getLeveledSpeed(),
                attributes.totalStrength(),
                attributes.totalSense(),
                attributes.totalSpeed(),
                attributes.health(),
                attributes.damage(),
                attributes.spellDamage(),
                attributes.armorRating(),
                attributes.critChance(),
                attributes.stamina(),
                attributes.stealth(),
                recalculatePointsAtLevelUp(pc),
                abilityList
        );
    }

    public CharacterAttributes recalculateAttributes(PlayerCharacter pc) {
        Species species = pc.getSpecies();

        int totalStrength = BASE_STRENGTH + species.getStrengthMod() + pc.getLeveledStrength();
        int totalSense    = BASE_SENSE    + species.getSenseMod()    + pc.getLeveledSense();
        int totalSpeed    = BASE_SPEED    + species.getSpeedMod()    + pc.getLeveledSpeed();

        int health     = (totalStrength / HEALTH_STRENGTH_DIVISOR) + BASE_HEALTH;
        int damage     = totalStrength + BASE_DAMAGE;
        int spellDamage = totalSense  + BASE_SPELL_DAMAGE;

        if (pc.getClassType() == ClassType.WARRIOR) {
            health += WARRIOR_HEALTH_BONUS;
            damage += WARRIOR_DAMAGE_BONUS;
        } else if (pc.getClassType() == ClassType.SORCERER) {
            spellDamage += SORCERER_SPELL_DAMAGE_BONUS;
        }

        int critChance = recalculateCritChance(totalStrength, totalSense, totalSpeed, pc.getClassType());
        int stamina    = BASE_STAMINA + (totalSpeed / SPEED_STAMINA_DIVISOR);
        int stealth    = totalSpeed + (totalSense / STEALTH_SENSE_DIVISOR);

        return new CharacterAttributes(
                totalStrength, totalSense, totalSpeed,
                health, damage, spellDamage,
                0, // armorRating — Phase 4
                critChance, stamina, stealth
        );
    }

    public int recalculatePointsAtLevelUp(PlayerCharacter pc) {
        return pc.getLevel() * LEVEL_UP_POINTS_MULTIPLIER;
    }

    private int recalculateCritChance(int strength, int sense, int speed, ClassType classType) {
        return switch (classType) {
            case WARRIOR  -> BASE_CRIT_CHANCE + (strength / CRIT_STAT_DIVISOR);
            case SORCERER -> BASE_CRIT_CHANCE + (sense    / CRIT_STAT_DIVISOR);
            case THIEF    -> BASE_CRIT_CHANCE + (speed    / CRIT_STAT_DIVISOR);
        };
    }
}
