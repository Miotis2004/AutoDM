package com.autodm.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbilitySkillResolutionServiceTest {

    private DiceService diceService;
    private AbilitySkillResolutionService service;

    @BeforeEach
    void setUp() {
        diceService = mock(DiceService.class);
        service = new AbilitySkillResolutionService(diceService);
    }

    @Test
    void testCalculateModifier() {
        assertEquals(-1, service.calculateModifier(8));
        assertEquals(0, service.calculateModifier(10));
        assertEquals(0, service.calculateModifier(11));
        assertEquals(1, service.calculateModifier(12));
        assertEquals(1, service.calculateModifier(13));
        assertEquals(2, service.calculateModifier(14));
        assertEquals(3, service.calculateModifier(16));
        assertEquals(5, service.calculateModifier(20));
    }

    @Test
    void testResolveCheck_Success() {
        when(diceService.rollD20()).thenReturn(15);

        // Score: 14 (+2), Prof: +2, Roll: 15, Total: 19, DC: 15
        CheckResult result = service.resolveCheck(14, 2, 15);

        assertEquals(15, result.baseRoll());
        assertEquals(4, result.modifier()); // 2 (ability) + 2 (prof)
        assertEquals(19, result.total());
        assertEquals(15, result.difficultyClass());
        assertTrue(result.isSuccess());
    }

    @Test
    void testResolveCheck_Failure() {
        when(diceService.rollD20()).thenReturn(8);

        // Score: 10 (+0), Prof: 0, Roll: 8, Total: 8, DC: 10
        CheckResult result = service.resolveCheck(10, 0, 10);

        assertEquals(8, result.baseRoll());
        assertEquals(0, result.modifier());
        assertEquals(8, result.total());
        assertEquals(10, result.difficultyClass());
        assertFalse(result.isSuccess());
    }

    @Test
    void testResolveCheck_Advantage() {
        when(diceService.rollD20()).thenReturn(5, 18);

        // Advantage should take 18
        CheckResult result = service.resolveCheck(12, 0, 15, true, false);

        assertEquals(18, result.baseRoll());
        assertEquals(1, result.modifier());
        assertEquals(19, result.total());
        assertTrue(result.isSuccess());

        verify(diceService, times(2)).rollD20();
    }

    @Test
    void testResolveCheck_Disadvantage() {
        when(diceService.rollD20()).thenReturn(15, 4);

        // Disadvantage should take 4
        CheckResult result = service.resolveCheck(12, 0, 10, false, true);

        assertEquals(4, result.baseRoll());
        assertEquals(1, result.modifier());
        assertEquals(5, result.total());
        assertFalse(result.isSuccess());

        verify(diceService, times(2)).rollD20();
    }

    @Test
    void testResolveCheck_AdvantageAndDisadvantageCancelOut() {
        when(diceService.rollD20()).thenReturn(12);

        // They cancel out, so only 1 roll
        CheckResult result = service.resolveCheck(12, 0, 10, true, true);

        assertEquals(12, result.baseRoll());

        verify(diceService, times(1)).rollD20();
    }
}
