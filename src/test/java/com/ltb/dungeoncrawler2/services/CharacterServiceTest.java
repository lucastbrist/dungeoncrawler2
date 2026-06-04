package com.ltb.dungeoncrawler2.services;

import com.ltb.dungeoncrawler2.enums.ClassType;
import com.ltb.dungeoncrawler2.models.CharacterAttributes;
import com.ltb.dungeoncrawler2.models.PlayerCharacter;
import com.ltb.dungeoncrawler2.models.Species;
import com.ltb.dungeoncrawler2.repositories.PlayerCharacterRepository;
import com.ltb.dungeoncrawler2.repositories.SpeciesRepository;
import com.ltb.dungeoncrawler2.repositories.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import static com.ltb.dungeoncrawler2.enums.ClassType.*;
import static com.ltb.dungeoncrawler2.services.CharacterService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock UserRepository userRepo;
    @Mock SpeciesRepository speciesRepo;
    @Mock PlayerCharacterRepository characterRepo;

    @InjectMocks CharacterService service;

    static Stream<Arguments> speciesAndClassCombinations() {
        // strMod, senseMod, spdMod (from V2 seed data), classType
        int[][] speciesMods = {
            { 1,  1,  1},  // Human
            {-1,  2,  2},  // Elf
            { 3, -2,  0},  // Dwarf
            {-2,  0,  3},  // Halfling
            {-3,  3,  1},  // Gnome
            {-1, -1, -1},  // Shamble
        };
        ClassType[] classes = {WARRIOR, SORCERER, THIEF};

        Stream.Builder<Arguments> args = Stream.builder();
        for (int[] mods : speciesMods) {
            for (ClassType classType : classes) {
                args.add(Arguments.of(mods[0], mods[1], mods[2], classType));
            }
        }
        return args.build();
    }

    @ParameterizedTest
    @MethodSource("speciesAndClassCombinations")
    void recalculateAttributes_formulasAreCorrect(int strMod, int senseMod, int spdMod, ClassType classType) {
        CharacterAttributes attributes = service.recalculateAttributes(characterWith(strMod, senseMod, spdMod, classType));

        int str   = BASE_STRENGTH + strMod;
        int sense = BASE_SENSE    + senseMod;
        int spd   = BASE_SPEED    + spdMod;

        int expectedHealth   = (str / HEALTH_STRENGTH_DIVISOR) + BASE_HEALTH
                               + (classType == WARRIOR ? WARRIOR_HEALTH_BONUS : 0);
        int expectedDamage   = str + BASE_DAMAGE
                               + (classType == WARRIOR ? WARRIOR_DAMAGE_BONUS : 0);
        int expectedSpellDmg = sense + BASE_SPELL_DAMAGE
                               + (classType == SORCERER ? SORCERER_SPELL_DAMAGE_BONUS : 0);
        int expectedCrit = switch (classType) {
            case WARRIOR  -> BASE_CRIT_CHANCE + (str / CRIT_STAT_DIVISOR);
            case SORCERER -> BASE_CRIT_CHANCE + (sense / CRIT_STAT_DIVISOR);
            case THIEF    -> BASE_CRIT_CHANCE + (spd / CRIT_STAT_DIVISOR);
        };
        int expectedStamina = BASE_STAMINA + (spd / SPEED_STAMINA_DIVISOR);
        int expectedStealth = spd + (sense / STEALTH_SENSE_DIVISOR);

        assertEquals(str,              attributes.totalStrength(), "totalStrength");
        assertEquals(sense,            attributes.totalSense(),    "totalSense");
        assertEquals(spd,              attributes.totalSpeed(),    "totalSpeed");
        assertEquals(expectedHealth,   attributes.health(),        "health");
        assertEquals(expectedDamage,   attributes.damage(),        "damage");
        assertEquals(expectedSpellDmg, attributes.spellDamage(),   "spellDamage");
        assertEquals(0,                attributes.armorRating(),   "armorRating");
        assertEquals(expectedCrit,     attributes.critChance(),    "critChance");
        assertEquals(expectedStamina,  attributes.stamina(),       "stamina");
        assertEquals(expectedStealth,  attributes.stealth(),       "stealth");
    }

    private static PlayerCharacter characterWith(int strMod, int senseMod, int spdMod, ClassType classType) {
        Species species = mock(Species.class);
        when(species.getStrengthMod()).thenReturn(strMod);
        when(species.getSenseMod()).thenReturn(senseMod);
        when(species.getSpeedMod()).thenReturn(spdMod);

        PlayerCharacter pc = new PlayerCharacter();
        pc.setSpecies(species);
        pc.setClassType(classType);
        return pc;
    }
}