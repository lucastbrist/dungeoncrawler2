package com.ltb.dungeoncrawler2.services;

import com.ltb.dungeoncrawler2.models.CharacterStats;
import com.ltb.dungeoncrawler2.models.PlayerCharacter;
import com.ltb.dungeoncrawler2.models.Species;
import com.ltb.dungeoncrawler2.repositories.PlayerCharacterRepository;
import com.ltb.dungeoncrawler2.repositories.SpeciesRepository;
import com.ltb.dungeoncrawler2.repositories.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import static com.ltb.dungeoncrawler2.services.CharacterService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock UserRepository userRepo;
    @Mock SpeciesRepository speciesRepo;
    @Mock PlayerCharacterRepository characterRepo;

    @InjectMocks CharacterService service;

    static Stream<Arguments> speciesMods() {
        // strMod, senseMod, spdMod (addendum-canonical V2 seed values)
        return Stream.of(
            Arguments.of( 1,  1,  1),  // Human
            Arguments.of( 0,  2,  1),  // Elf
            Arguments.of( 3,  0,  0),  // Dwarf
            Arguments.of(-3,  0,  3),  // Halfling
            Arguments.of(-3,  3,  0),  // Gnome
            Arguments.of( 2, -1,  2),  // Beastfolk
            Arguments.of(-3, -3, -3)   // Shamble
        );
    }

    @ParameterizedTest
    @MethodSource("speciesMods")
    void recalculateStats_formulasAreCorrect(int strMod, int senseMod, int spdMod) {
        CharacterStats stats = service.recalculateStats(characterWith(strMod, senseMod, spdMod));

        int str   = BASE_ATTRIBUTE_VALUE + strMod;
        int sense = BASE_ATTRIBUTE_VALUE + senseMod;
        int spd   = BASE_ATTRIBUTE_VALUE + spdMod;

        int   expectedHealth          = BASE_POOL + str   * ATTRIBUTE_MAIN_SCALAR_MULT;
        int   expectedArcana          = BASE_POOL + sense * ATTRIBUTE_MAIN_SCALAR_MULT;
        int   expectedStamina         = BASE_POOL + spd   * ATTRIBUTE_MAIN_SCALAR_MULT;
        int   expectedMagicDamage     = sense * ATTRIBUTE_SECOND_SCALAR_MULT;
        int   expectedCarryWeight     = BASE_CARRY_WEIGHT + str * ATTRIBUTE_SECOND_SCALAR_MULT;
        int   expectedInitiative      = spd * ATTRIBUTE_SECOND_SCALAR_MULT;
        int   expectedPhysicalDamage  = (int)(str * SHARED_SCALAR + spd * SHARED_SCALAR);
        int   expectedCritThreshold   = D100 - (int)(sense * SHARED_SCALAR + spd * SHARED_SCALAR);
        float expectedCritDamageMult  = CRIT_BASE_MULTIPLIER
                + (str * SHARED_SCALAR + sense * SHARED_SCALAR) * CRIT_DAMAGE_COEFFICIENT;

        assertEquals(str,                  stats.totalStrength(),       "totalStrength");
        assertEquals(sense,                stats.totalSense(),           "totalSense");
        assertEquals(spd,                  stats.totalSpeed(),           "totalSpeed");
        assertEquals(expectedHealth,       stats.health(),               "health");
        assertEquals(expectedArcana,       stats.arcana(),               "arcana");
        assertEquals(expectedStamina,      stats.stamina(),              "stamina");
        assertEquals(expectedMagicDamage,  stats.magicDamage(),          "magicDamage");
        assertEquals(expectedCarryWeight,  stats.carryWeight(),          "carryWeight");
        assertEquals(expectedInitiative,   stats.initiative(),           "initiative");
        assertEquals(expectedPhysicalDamage, stats.physicalDamage(),     "physicalDamage");
        assertEquals(expectedCritThreshold,  stats.critThreshold(),      "critThreshold");
        assertEquals(0,                    stats.armorDR(),          "armorDR");
        assertEquals(0,                    stats.armorDT(),          "armorDT");
        assertEquals(expectedCritDamageMult, stats.critDamageMultiplier(), 0.0001f, "critDamageMultiplier");
    }

    private static PlayerCharacter characterWith(int strMod, int senseMod, int spdMod) {
        Species species = mock(Species.class);
        when(species.getStrengthMod()).thenReturn(strMod);
        when(species.getSenseMod()).thenReturn(senseMod);
        when(species.getSpeedMod()).thenReturn(spdMod);

        PlayerCharacter pc = new PlayerCharacter();
        pc.setSpecies(species);
        return pc;
    }
}