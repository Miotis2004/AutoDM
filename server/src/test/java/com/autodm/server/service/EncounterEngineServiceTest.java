package com.autodm.server.service;

import com.autodm.server.model.Combatant;
import com.autodm.server.model.Encounter;
import com.autodm.server.model.EncounterStatus;
import com.autodm.server.repository.CombatantRepository;
import com.autodm.server.repository.EncounterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EncounterEngineServiceTest {

    @Mock
    private EncounterRepository encounterRepository;

    @Mock
    private CombatantRepository combatantRepository;

    @Mock
    private DiceService diceService;

    @Mock
    private EnemyBehaviorService enemyBehaviorService;

    @InjectMocks
    private EncounterEngineService encounterEngineService;

    private Encounter encounter;
    private Combatant c1;
    private Combatant c2;
    private Combatant c3;

    @BeforeEach
    void setUp() {
        encounter = new Encounter();
        encounter.setId(1L);
        encounter.setStatus(EncounterStatus.PENDING);

        c1 = new Combatant();
        c1.setId(1L);
        c1.setEncounter(encounter);
        c1.setIsPlayer(true);
        c1.setInitiative(15);
        c1.setIsDefeated(false);

        c2 = new Combatant();
        c2.setId(2L);
        c2.setEncounter(encounter);
        c2.setIsPlayer(false);
        c2.setInitiative(10);
        c2.setIsDefeated(false);

        c3 = new Combatant();
        c3.setId(3L);
        c3.setEncounter(encounter);
        c3.setIsPlayer(false);
        c3.setInitiative(5);
        c3.setIsDefeated(false);
    }

    @Test
    void testStartEncounter() {
        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));

        Combatant noInitCombatant = new Combatant();
        noInitCombatant.setId(4L);
        noInitCombatant.setEncounter(encounter);
        noInitCombatant.setIsPlayer(false);
        noInitCombatant.setIsDefeated(false);

        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(1L))
            .thenReturn(Arrays.asList(c1, c2, c3, noInitCombatant)) // initial call
            .thenReturn(Arrays.asList(c1, c2, c3, noInitCombatant)); // second call after rolling init

        when(diceService.roll(1, 20, 0)).thenReturn(new RollResult(12, List.of(12), 0));
        when(encounterRepository.save(any(Encounter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encounter started = encounterEngineService.startEncounter(1L);

        assertEquals(EncounterStatus.ACTIVE, started.getStatus());
        assertEquals(1, started.getCurrentTurn());
        assertEquals(c1.getId(), started.getActiveCombatantId());

        verify(combatantRepository, times(1)).save(noInitCombatant);
        assertEquals(12, noInitCombatant.getInitiative());
    }

    @Test
    void testAdvanceTurn() {
        encounter.setStatus(EncounterStatus.ACTIVE);
        encounter.setCurrentTurn(1);
        encounter.setActiveCombatantId(c1.getId());

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(1L)).thenReturn(Arrays.asList(c1, c2, c3));
        when(encounterRepository.save(any(Encounter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encounter advanced = encounterEngineService.advanceTurn(1L);

        assertEquals(c2.getId(), advanced.getActiveCombatantId());
        assertEquals(1, advanced.getCurrentTurn());
    }

    @Test
    void testAdvanceTurnWrapAround() {
        encounter.setStatus(EncounterStatus.ACTIVE);
        encounter.setCurrentTurn(1);
        encounter.setActiveCombatantId(c3.getId()); // Last combatant

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(1L)).thenReturn(Arrays.asList(c1, c2, c3));
        when(encounterRepository.save(any(Encounter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encounter advanced = encounterEngineService.advanceTurn(1L);

        assertEquals(c1.getId(), advanced.getActiveCombatantId());
        assertEquals(2, advanced.getCurrentTurn()); // Should increment round
    }

    @Test
    void testAdvanceTurnSkipsDefeated() {
        encounter.setStatus(EncounterStatus.ACTIVE);
        encounter.setCurrentTurn(1);
        encounter.setActiveCombatantId(c1.getId());

        c2.setIsDefeated(true);

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(1L)).thenReturn(Arrays.asList(c1, c2, c3));
        when(encounterRepository.save(any(Encounter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encounter advanced = encounterEngineService.advanceTurn(1L);

        assertEquals(c3.getId(), advanced.getActiveCombatantId());
        assertEquals(1, advanced.getCurrentTurn());
    }

    @Test
    void testCheckEncounterCompletionAllEnemiesDefeated() {
        encounter.setStatus(EncounterStatus.ACTIVE);
        c2.setIsDefeated(true);
        c3.setIsDefeated(true);

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(1L)).thenReturn(Arrays.asList(c1, c2, c3));
        when(encounterRepository.save(any(Encounter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encounter advanced = encounterEngineService.advanceTurn(1L);

        assertEquals(EncounterStatus.COMPLETED, advanced.getStatus());
    }

    @Test
    void testCheckEncounterCompletionAllPlayersDefeated() {
        encounter.setStatus(EncounterStatus.ACTIVE);
        c1.setIsDefeated(true);

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(1L)).thenReturn(Arrays.asList(c1, c2, c3));
        when(encounterRepository.save(any(Encounter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encounter advanced = encounterEngineService.advanceTurn(1L);

        assertEquals(EncounterStatus.COMPLETED, advanced.getStatus());
    }

    @Test
    void testExecuteCurrentTurnEnemy() {
        encounter.setStatus(EncounterStatus.ACTIVE);
        encounter.setActiveCombatantId(c2.getId());

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));
        when(combatantRepository.findById(c2.getId())).thenReturn(Optional.of(c2));
        when(enemyBehaviorService.executeEnemyTurn(1L, c2.getId())).thenReturn("Goblin attacks Fighter. Hit!");

        String result = encounterEngineService.executeCurrentTurn(1L);

        assertEquals("Goblin attacks Fighter. Hit!", result);
        verify(enemyBehaviorService, times(1)).executeEnemyTurn(1L, c2.getId());
    }

    @Test
    void testExecuteCurrentTurnPlayer() {
        encounter.setStatus(EncounterStatus.ACTIVE);
        encounter.setActiveCombatantId(c1.getId());

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));
        when(combatantRepository.findById(c1.getId())).thenReturn(Optional.of(c1));

        String result = encounterEngineService.executeCurrentTurn(1L);

        assertNull(result);
        verify(enemyBehaviorService, never()).executeEnemyTurn(any(), any());
    }
}
