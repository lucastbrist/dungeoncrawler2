package com.ltb.dungeoncrawler2.services;

import com.ltb.dungeoncrawler2.enums.ClassType;
import com.ltb.dungeoncrawler2.models.CharacterStats;
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
        // strMod, senseMod, spdMod (addendum-canonical V2 seed values)
        int[][] speciesMods = {
            { 1,  1,  1},  // Human
            { 0,  2,  1},  // Elf
            { 3,  0,  0},  // Dwarf
            {-3,  0,  3},  // Halfling
            {-3,  3,  0},  // Gnome
            {-3, -3, -3},  // Shamble
            { 2, -1,  2},  // Beastfolk
        };
        ClassType[] classes = {WARRIOR, SORCERER, BURGLAR};

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
        CharacterStats stats = service.recalculateStats(characterWith(strMod, senseMod, spdMod, classType));

        int str   = BASE_STRENGTH + strMod;
        int sense = BASE_SENSE    + senseMod;
        int spd   = BASE_SPEED    + spdMod;

        int expectedHealth   = (str / HEALTH_STRENGTH_DIVISOR) + BASE_HEALTH
                               + (classType == WARRIOR ? WARRIOR_HEALTH_BONUS : 0);
        int expectedDamage   = str + BASE_DAMAGE
                               + (classType == WARRIOR ? WARRIOR_DAMAGE_BONUS : 0);
        int expectedSpellDmg = sense + BASE_SPELL_DAMAGE
                               + (classType == SORCERER ? SORCERER_SPELL_DAMAGE_BONUS : 0);
        int expectedCrit     = BASE_CRIT_CHANCE + (sense / CRIT_STAT_DIVISOR);
        int expectedStamina  = BASE_STAMINA + (spd / SPEED_STAMINA_DIVISOR);
        int expectedStealth  = spd + (sense / STEALTH_SENSE_DIVISOR);

        assertEquals(str,              stats.totalStrength(), "totalStrength");
        assertEquals(sense,            stats.totalSense(),    "totalSense");
        assertEquals(spd,              stats.totalSpeed(),    "totalSpeed");
        assertEquals(expectedHealth,   stats.health(),        "health");
        assertEquals(expectedDamage,   stats.damage(),        "damage");
        assertEquals(expectedSpellDmg, stats.spellDamage(),   "spellDamage");
        assertEquals(0,                stats.armorRating(),   "armorRating");
        assertEquals(expectedCrit,     stats.critChance(),    "critChance");
        assertEquals(expectedStamina,  stats.stamina(),       "stamina");
        assertEquals(expectedStealth,  stats.stealth(),       "stealth");
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