package com.autodm.server.service;

import org.springframework.stereotype.Service;

@Service
public class AbilitySkillResolutionService {

    private final DiceService diceService;

    public AbilitySkillResolutionService(DiceService diceService) {
        this.diceService = diceService;
    }

    /**
     * Resolves an ability or skill check against a difficulty class.
     *
     * @param abilityScore The raw ability score (e.g., 10, 16)
     * @param proficiencyBonus Any additional bonus (proficiency, items, etc.)
     * @param difficultyClass The target number to meet or exceed
     * @param advantage If true, roll twice and take the higher. If disadvantage is also true, they cancel out.
     * @param disadvantage If true, roll twice and take the lower.
     * @return The result of the check
     */
    public CheckResult resolveCheck(int abilityScore, int proficiencyBonus, int difficultyClass, boolean advantage, boolean disadvantage) {
        int modifier = calculateModifier(abilityScore) + proficiencyBonus;

        int baseRoll = rollD20(advantage, disadvantage);
        int total = baseRoll + modifier;

        // Critical success/failure on skill checks is not strictly RAW in 5e for skills,
        // but we'll stick to simple comparison: total >= DC
        boolean isSuccess = total >= difficultyClass;

        return new CheckResult(baseRoll, modifier, total, difficultyClass, isSuccess);
    }

    /**
     * Resolves a simple check without advantage/disadvantage.
     */
    public CheckResult resolveCheck(int abilityScore, int proficiencyBonus, int difficultyClass) {
        return resolveCheck(abilityScore, proficiencyBonus, difficultyClass, false, false);
    }

    /**
     * Standard D&D 5e formula for ability modifiers: (score - 10) / 2, rounded down.
     */
    public int calculateModifier(int score) {
        return Math.floorDiv(score - 10, 2);
    }

    private int rollD20(boolean advantage, boolean disadvantage) {
        if (advantage && !disadvantage) {
            int roll1 = diceService.rollD20();
            int roll2 = diceService.rollD20();
            return Math.max(roll1, roll2);
        } else if (disadvantage && !advantage) {
            int roll1 = diceService.rollD20();
            int roll2 = diceService.rollD20();
            return Math.min(roll1, roll2);
        } else {
            // Normal roll (or advantage and disadvantage cancel out)
            return diceService.rollD20();
        }
    }
}
