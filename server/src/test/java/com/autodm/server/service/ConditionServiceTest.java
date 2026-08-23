package com.autodm.server.service;

import com.autodm.server.model.Combatant;
import com.autodm.server.model.Condition;
import com.autodm.server.repository.ConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConditionServiceTest {

    @Mock
    private ConditionRepository conditionRepository;

    @InjectMocks
    private ConditionService conditionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplyCondition() {
        Combatant combatant = new Combatant();
        combatant.setId(1L);

        Condition savedCondition = new Condition();
        savedCondition.setId(10L);
        savedCondition.setCombatant(combatant);
        savedCondition.setName("Poisoned");
        savedCondition.setDescription("Disadvantage on attack rolls.");
        savedCondition.setDuration(3);
        savedCondition.setSource("Snake");
        savedCondition.setIsActive(true);

        when(conditionRepository.save(any(Condition.class))).thenReturn(savedCondition);

        Condition result = conditionService.applyCondition(combatant, "Poisoned", "Disadvantage on attack rolls.", 3, "Snake");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Poisoned", result.getName());
        assertEquals(3, result.getDuration());
        assertTrue(result.getIsActive());
        verify(conditionRepository, times(1)).save(any(Condition.class));
    }

    @Test
    void testDeactivateCondition() {
        Condition activeCondition = new Condition();
        activeCondition.setId(1L);
        activeCondition.setIsActive(true);

        when(conditionRepository.findById(1L)).thenReturn(Optional.of(activeCondition));
        when(conditionRepository.save(any(Condition.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Condition> result = conditionService.deactivateCondition(1L);

        assertTrue(result.isPresent());
        assertFalse(result.get().getIsActive());
        verify(conditionRepository, times(1)).findById(1L);
        verify(conditionRepository, times(1)).save(activeCondition);
    }

    @Test
    void testGetActiveConditions() {
        Condition c1 = new Condition();
        c1.setIsActive(true);
        Condition c2 = new Condition();
        c2.setIsActive(false);
        Condition c3 = new Condition();
        c3.setIsActive(true);

        when(conditionRepository.findByCombatantId(1L)).thenReturn(Arrays.asList(c1, c2, c3));

        List<Condition> activeConditions = conditionService.getActiveConditions(1L);

        assertEquals(2, activeConditions.size());
        assertTrue(activeConditions.contains(c1));
        assertTrue(activeConditions.contains(c3));
        verify(conditionRepository, times(1)).findByCombatantId(1L);
    }

    @Test
    void testProcessTurnEnd_DecrementsDurationAndDeactivates() {
        Condition c1 = new Condition();
        c1.setId(1L);
        c1.setIsActive(true);
        c1.setDuration(2);

        Condition c2 = new Condition();
        c2.setId(2L);
        c2.setIsActive(true);
        c2.setDuration(1); // Should become 0 and deactivate

        Condition c3 = new Condition();
        c3.setId(3L);
        c3.setIsActive(true);
        c3.setDuration(null); // Should remain untouched

        when(conditionRepository.findByCombatantId(1L)).thenReturn(Arrays.asList(c1, c2, c3));
        when(conditionRepository.save(any(Condition.class))).thenAnswer(invocation -> invocation.getArgument(0));

        conditionService.processTurnEnd(1L);

        assertEquals(1, c1.getDuration());
        assertTrue(c1.getIsActive());

        assertEquals(0, c2.getDuration());
        assertFalse(c2.getIsActive());

        assertNull(c3.getDuration());
        assertTrue(c3.getIsActive());

        verify(conditionRepository, times(1)).save(c1);
        verify(conditionRepository, times(1)).save(c2);
        verify(conditionRepository, never()).save(c3);
    }
}
