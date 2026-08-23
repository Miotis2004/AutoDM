package com.autodm.server.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiceServiceTest {

    private final DiceService diceService = new DiceService();

    @Test
    public void testRollBounds() {
        for (int i = 0; i < 1000; i++) {
            int d4 = diceService.rollD4();
            assertTrue(d4 >= 1 && d4 <= 4);

            int d20 = diceService.rollD20();
            assertTrue(d20 >= 1 && d20 <= 20);

            int percentile = diceService.rollPercentile();
            assertTrue(percentile >= 1 && percentile <= 100);
        }
    }

    @Test
    public void testRollMultipleDiceWithModifier() {
        RollResult result = diceService.roll(3, 6, 2);
        assertEquals(3, result.getRolls().size());
        assertEquals(2, result.getModifier());

        int sum = result.getRolls().stream().mapToInt(Integer::intValue).sum();
        assertEquals(sum + 2, result.getTotal());

        for (int roll : result.getRolls()) {
            assertTrue(roll >= 1 && roll <= 6);
        }
    }

    @Test
    public void testRollStringNotation() {
        RollResult result = diceService.roll("2d8+3");
        assertEquals(2, result.getRolls().size());
        assertEquals(3, result.getModifier());
        int sum = result.getRolls().stream().mapToInt(Integer::intValue).sum();
        assertEquals(sum + 3, result.getTotal());
        for (int roll : result.getRolls()) {
            assertTrue(roll >= 1 && roll <= 8);
        }

        RollResult resultNoMod = diceService.roll("1d20");
        assertEquals(1, resultNoMod.getRolls().size());
        assertEquals(0, resultNoMod.getModifier());
        assertEquals(resultNoMod.getRolls().get(0).intValue(), resultNoMod.getTotal());

        RollResult resultNegativeMod = diceService.roll("3d4 - 1");
        assertEquals(3, resultNegativeMod.getRolls().size());
        assertEquals(-1, resultNegativeMod.getModifier());
        int sumNeg = resultNegativeMod.getRolls().stream().mapToInt(Integer::intValue).sum();
        assertEquals(sumNeg - 1, resultNegativeMod.getTotal());

        RollResult resultImpliedCount = diceService.roll("d6+1");
        assertEquals(1, resultImpliedCount.getRolls().size());
        assertEquals(1, resultImpliedCount.getModifier());
    }

    @Test
    public void testInvalidRollNotation() {
        assertThrows(IllegalArgumentException.class, () -> diceService.roll("abc"));
        assertThrows(IllegalArgumentException.class, () -> diceService.roll("2d-1"));
        assertThrows(IllegalArgumentException.class, () -> diceService.roll("0d6"));
    }
}
