package com.ltb.dungeoncrawler2.services;

import com.ltb.dungeoncrawler2.enums.AbilityCategory;
import com.ltb.dungeoncrawler2.enums.AttributeType;
import com.ltb.dungeoncrawler2.enums.ClassType;
import com.ltb.dungeoncrawler2.enums.GameMode;
import com.ltb.dungeoncrawler2.enums.SkillType;
import com.ltb.dungeoncrawler2.enums.SpeciesName;
import com.ltb.dungeoncrawler2.exceptions.BadRequestException;
import com.ltb.dungeoncrawler2.exceptions.NotFoundException;
import com.ltb.dungeoncrawler2.models.Ability;
import com.ltb.dungeoncrawler2.models.CharacterAbility;
import com.ltb.dungeoncrawler2.models.CharacterStats;
import com.ltb.dungeoncrawler2.models.PlayerCharacter;
import com.ltb.dungeoncrawler2.models.Species;
import com.ltb.dungeoncrawler2.models.User;
import com.ltb.dungeoncrawler2.models.dto.AbilityInfo;
import com.ltb.dungeoncrawler2.models.dto.AttributeDiscountInfo;
import com.ltb.dungeoncrawler2.models.dto.CharacterResponse;
import com.ltb.dungeoncrawler2.models.dto.SkillDiscountInfo;
import com.ltb.dungeoncrawler2.models.dto.SpeciesInfo;
import com.ltb.dungeoncrawler2.repositories.PlayerCharacterRepository;
import com.ltb.dungeoncrawler2.repositories.SpeciesRepository;
import com.ltb.dungeoncrawler2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterService {

    // --- Attribute system ---
    public static final int   BASE_ATTRIBUTE_VALUE        = 3;
    public static final int   ATTRIBUTE_CAP               = 30;   
    public static final int   LEVEL_CAP                   = 30;   // level cap and attribute cap are intrinsically tied
    public static final int   ATTRIBUTE_POINTS_PER_LEVEL  = 2;    // flat per level-up (levels 2-30)
    
    // Range 1-3 only; values outside this range break the math at level cap 30
    // with 2 points per level and BASE_ATTRIBUTE_VALUE 3.
    public static final int   CHARGEN_ATTRIBUTE_POINTS    = 3;

    // --- Scalars: symmetry of all values within is vital ---
    // Resource pool formula: (BASE_POOL + attr × ATTRIBUTE_MAIN_SCALAR_MULT)
    public static final int   ATTRIBUTE_MAIN_SCALAR_MULT   = 3;
    // Second sole scalars: carry weight, magic damage, initiative; must all stay equal
    public static final int   ATTRIBUTE_SECOND_SCALAR_MULT = 2;
    // Shared scalars: sole-attribute weapons use 2 × SHARED_SCALAR = 1.0 instead
    public static final float SHARED_SCALAR               = 0.5f;
    
    public static final int   BASE_POOL                   = 100;
    public static final int   BASE_CARRY_WEIGHT           = 25;   // kg

    // --- Roll system ---
    public static final int   D100                        = 100;

    // --- Critical system ---
    public static final float CRIT_BASE_MULTIPLIER        = 2.0f;
    public static final float CRIT_DAMAGE_COEFFICIENT     = 0.1f;

    // --- Skills ---
    public static final int   SKILL_CAP                   = 100;  // Ironclad; values any higher begin to break the game math
    public static final int   SKILL_BASE_VALUE            = 1;    // non-vocation track starting value
    public static final int   VOCATION_SKILL_BASE_VALUE   = 10;    // vocation track starting value

    private static final Set<SkillType> WARRIOR_SKILLS  = EnumSet.of(SkillType.MELEE, SkillType.MISSILES, SkillType.ARMOR);
    private static final Set<SkillType> SORCERER_SKILLS = EnumSet.of(SkillType.PROJECTION, SkillType.PERCEPTION, SkillType.INFUSION);
    private static final Set<SkillType> BURGLAR_SKILLS  = EnumSet.of(SkillType.SUBTERFUGE, SkillType.EVASION, SkillType.CONDITIONING);

    private final UserRepository userRepo;
    private final SpeciesRepository speciesRepo;
    private final PlayerCharacterRepository characterRepo;

    // WithAbilities variants used here because createCharacter grants species abilities to the character.
    // Plain findById/findByName would leave species.abilities as an unloaded proxy outside the fetch boundary.

    @Transactional
    public CharacterResponse createCharacter(Long userId, String name, Long speciesId, ClassType classType,
                                             GameMode mode, SkillType discountedSkill,
                                             List<AttributeType> chosenAttributeDiscounts) {
        Species species = speciesRepo.findByIdWithAbilities(speciesId)
                .orElseThrow(() -> new NotFoundException("Species not found: " + speciesId));
        return createCharacter(userId, name, species, classType, mode, discountedSkill, chosenAttributeDiscounts);
    }

    @Transactional
    public CharacterResponse createCharacter(Long userId, String name, SpeciesName speciesName, ClassType classType,
                                             GameMode mode, SkillType discountedSkill,
                                             List<AttributeType> chosenAttributeDiscounts) {
        Species species = speciesRepo.findByNameWithAbilities(speciesName)
                .orElseThrow(() -> new NotFoundException("Species not found: " + speciesName));
        return createCharacter(userId, name, species, classType, mode, discountedSkill, chosenAttributeDiscounts);
    }

    private CharacterResponse createCharacter(Long userId, String name, Species species, ClassType classType,
                                              GameMode mode, SkillType discountedSkill,
                                              List<AttributeType> chosenAttributeDiscounts) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        validateVocationSkill(classType, discountedSkill);

        PlayerCharacter pc = new PlayerCharacter();
        pc.setUser(user);
        pc.setName(name);
        pc.setSpecies(species);
        pc.setClassType(classType);
        pc.setMode(mode);
        pc.setDiscountedSkill(discountedSkill);

        List<Ability> learningRateAbilities = new ArrayList<>();
        for (Ability ability : species.getAbilities()) {
            if (ability.getCategory() == AbilityCategory.LEARNING_RATE)
                learningRateAbilities.add(ability);
            else
                grantAbility(pc, ability, ability.getInitialUses());
        }

        if (species.getName() == SpeciesName.HUMAN) {
            grantHumanAttributeDiscounts(pc, learningRateAbilities, chosenAttributeDiscounts);
        } else {
            if (chosenAttributeDiscounts != null && !chosenAttributeDiscounts.isEmpty())
                throw new BadRequestException(species.getName() + " characters do not choose attribute discounts");
            for (Ability ability : learningRateAbilities)
                grantAbility(pc, ability, ability.getInitialUses());
        }

        return toResponse(characterRepo.save(pc));
    }

    private void validateVocationSkill(ClassType classType, SkillType vocationSkill) {
        Set<SkillType> track = switch (classType) {
            case WARRIOR  -> WARRIOR_SKILLS;
            case SORCERER -> SORCERER_SKILLS;
            case BURGLAR  -> BURGLAR_SKILLS;
        };
        if (!track.contains(vocationSkill))
            throw new BadRequestException(
                    classType + " discountedSkill must be one of " + track + ", got: " + vocationSkill);
    }

    private void grantAbility(PlayerCharacter pc, Ability ability, int uses) {
        CharacterAbility grantedAbility = new CharacterAbility();
        grantedAbility.setPlayerCharacter(pc);
        grantedAbility.setAbility(ability);
        grantedAbility.setUsesRemaining(uses);
        pc.getAbilities().add(grantedAbility);
    }

    private void grantHumanAttributeDiscounts(PlayerCharacter pc, List<Ability> learningRateAbilities,
                                              List<AttributeType> chosenAttributeDiscounts) {
        if (chosenAttributeDiscounts == null || chosenAttributeDiscounts.size() != 2)
            throw new BadRequestException("Human characters must choose two Attributes to receive a learning rate increase");
        if (chosenAttributeDiscounts.get(0).equals(chosenAttributeDiscounts.get(1)))
            throw new BadRequestException("Human characters must choose two distinct Attributes");
        for (AttributeType attr : chosenAttributeDiscounts) {
            Ability ability = learningRateAbilities.stream()
                    .filter(a -> a.getEffectConfig() != null
                              && "ATTRIBUTE".equals(a.getEffectConfig().get("targetType"))
                              && attr.name().equals((String)a.getEffectConfig().get("target")))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Missing seed data: 2× ATTRIBUTE LR ability not in Human species_abilities for: " + attr));
            grantAbility(pc, ability, ability.getInitialUses());
        }
    }

    @Transactional(readOnly = true)
    public Optional<CharacterResponse> getCharacter(Long id) {
        return characterRepo.findByIdWithSpeciesAndAbilities(id).map(this::toResponse);
    }

    private CharacterResponse toResponse(PlayerCharacter pc) {
        CharacterStats stats = recalculateStats(pc);

        List<AbilityInfo> abilities = new ArrayList<>();
        List<AttributeDiscountInfo> attributeDiscounts = new ArrayList<>();
        List<SkillDiscountInfo> skillDiscounts = new ArrayList<>();

        for (CharacterAbility ca : pc.getAbilities()) {
            Ability ability = ca.getAbility();
            if (ability.getCategory() != AbilityCategory.LEARNING_RATE) {
                abilities.add(new AbilityInfo(ability.getId(), ability.getName(), ca.getUsesRemaining(), ability.getCategory()));
                continue;
            }
            mapLearningRateAbility(ca, attributeDiscounts, skillDiscounts);
        }

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
                stats.totalStrength(),
                stats.totalSense(),
                stats.totalSpeed(),
                stats.health(),
                stats.arcana(),
                stats.stamina(),
                stats.physicalDamage(),
                stats.magicDamage(),
                stats.armorDR(),
                stats.armorDT(),
                stats.critThreshold(),
                stats.critDamageMultiplier(),
                stats.carryWeight(),
                stats.initiative(),
                recalculatePointsAtLevelUp(),
                abilities,
                attributeDiscounts,
                skillDiscounts
        );
    }

    private void mapLearningRateAbility(CharacterAbility ca, List<AttributeDiscountInfo> attributeDiscounts,
                                        List<SkillDiscountInfo> skillDiscounts) {
        Ability ability = ca.getAbility();
        Map<String, Object> config = ability.getEffectConfig();
        if (config == null)
            throw new IllegalStateException("Malformed LEARNING_RATE ability " + ability.getId() + ": effect_config is null");
        String targetType = (String)config.get("targetType");
        String target     = (String)config.get("target");
        Number multiplier = (Number)config.get("multiplier");
        if (targetType == null || target == null || multiplier == null)
            throw new IllegalStateException("Malformed LEARNING_RATE ability " + ability.getId() + ": missing 'targetType', 'target', or 'multiplier'");
        switch (targetType.toUpperCase(Locale.ROOT)) {
            case "ATTRIBUTE" -> {
                try {
                    attributeDiscounts.add(new AttributeDiscountInfo(AttributeType.valueOf(target), multiplier.floatValue()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalStateException("Malformed LEARNING_RATE ability " + ability.getId() + ": unknown ATTRIBUTE value '" + target + "'");
                }
            }
            case "SKILL" -> {
                try {
                    skillDiscounts.add(new SkillDiscountInfo(SkillType.valueOf(target), multiplier.floatValue()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalStateException("Malformed LEARNING_RATE ability " + ability.getId() + ": unknown SKILL value '" + target + "'");
                }
            }
            default -> throw new IllegalStateException("Malformed LEARNING_RATE ability " + ability.getId() + ": unknown targetType '" + targetType + "'");
        }
    }

    public CharacterStats recalculateStats(PlayerCharacter pc) {
        Species species = pc.getSpecies();

        int totalStrength = BASE_ATTRIBUTE_VALUE + species.getStrengthMod() + pc.getLeveledStrength();
        int totalSense    = BASE_ATTRIBUTE_VALUE + species.getSenseMod()    + pc.getLeveledSense();
        int totalSpeed    = BASE_ATTRIBUTE_VALUE + species.getSpeedMod()    + pc.getLeveledSpeed();

        // Tier 1: resource pools
        int health  = BASE_POOL + totalStrength * ATTRIBUTE_MAIN_SCALAR_MULT;
        int arcana  = BASE_POOL + totalSense    * ATTRIBUTE_MAIN_SCALAR_MULT;
        int stamina = BASE_POOL + totalSpeed    * ATTRIBUTE_MAIN_SCALAR_MULT;

        // Tier 2: second sole scalars
        int magicDamage = totalSense     * ATTRIBUTE_SECOND_SCALAR_MULT;
        int carryWeight = BASE_CARRY_WEIGHT + totalStrength * ATTRIBUTE_SECOND_SCALAR_MULT;
        int initiative  = totalSpeed * ATTRIBUTE_SECOND_SCALAR_MULT;

        // Tier 3: shared scalars
        int   physicalDamage       = (int)(totalStrength * SHARED_SCALAR + totalSpeed * SHARED_SCALAR);
        int   critThreshold        = D100 - (int)(totalSense * SHARED_SCALAR + totalSpeed * SHARED_SCALAR);
        float critDamageMultiplier = CRIT_BASE_MULTIPLIER
                + (totalStrength * SHARED_SCALAR + totalSense * SHARED_SCALAR) * CRIT_DAMAGE_COEFFICIENT;

        return new CharacterStats(
                totalStrength, totalSense, totalSpeed,
                health, arcana, stamina,
                physicalDamage, magicDamage,
                0.0f,  
                0,     
                critThreshold, critDamageMultiplier,
                carryWeight, initiative
        );
    }

    public int recalculatePointsAtLevelUp() {
        return ATTRIBUTE_POINTS_PER_LEVEL;
    }
}