package com.autodm.server.service;

import com.autodm.server.model.Combatant;
import com.autodm.server.model.CreatureTemplate;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CombatantRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnemyBehaviorServiceTest {

    @Mock
    private CombatantRepository combatantRepository;

    @Mock
    private DiceService diceService;

    @InjectMocks
    private EnemyBehaviorService enemyBehaviorService;

    private Combatant enemy;
    private Combatant player;

    @BeforeEach
    void setUp() {
        enemy = new Combatant();
        enemy.setId(1L);
        enemy.setName("Goblin");
        enemy.setIsPlayer(false);
        enemy.setIsDefeated(false);

        CreatureTemplate template = new CreatureTemplate();
        template.setAttackBonus(4);
        template.setDamage("1d6+2");
        enemy.setTemplate(template);

        player = new Combatant();
        player.setId(2L);
        player.setName("Fighter");
        player.setIsPlayer(true);
        player.setIsDefeated(false);
        player.setHitPoints(10);

        PlayerCharacter pc = new PlayerCharacter();
        pc.setArmorClass(15);
        player.setPlayerCharacter(pc);
    }

    @Test
    void testExecuteTurnHit() {
        when(combatantRepository.findById(1L)).thenReturn(Optional.of(enemy));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(any())).thenReturn(List.of(player));

        // Attack roll: 12 + 4 = 16 (Hits AC 15)
        when(diceService.roll(1, 20, 4)).thenReturn(new RollResult(16, List.of(12), 4));

        // Damage roll: 5 total
        when(diceService.roll("1d6+2")).thenReturn(new RollResult(5, List.of(3), 2));

        String result = enemyBehaviorService.executeEnemyTurn(10L, 1L);

        assertTrue(result.contains("Hit!"));
        assertTrue(result.contains("Deals 5 damage"));
        assertEquals(5, player.getHitPoints());
        assertFalse(player.getIsDefeated());
        verify(combatantRepository).save(player);
    }

    @Test
    void testExecuteTurnMiss() {
        when(combatantRepository.findById(1L)).thenReturn(Optional.of(enemy));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(any())).thenReturn(List.of(player));

        // Attack roll: 8 + 4 = 12 (Misses AC 15)
        when(diceService.roll(1, 20, 4)).thenReturn(new RollResult(12, List.of(8), 4));

        String result = enemyBehaviorService.executeEnemyTurn(10L, 1L);

        assertTrue(result.contains("Misses"));
        assertEquals(10, player.getHitPoints());
        verify(combatantRepository, never()).save(any());
    }

    @Test
    void testExecuteTurnDefeatsTarget() {
        player.setHitPoints(3);

        when(combatantRepository.findById(1L)).thenReturn(Optional.of(enemy));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(any())).thenReturn(List.of(player));

        // Attack hits
        when(diceService.roll(1, 20, 4)).thenReturn(new RollResult(16, List.of(12), 4));

        // Damage: 5 total
        when(diceService.roll("1d6+2")).thenReturn(new RollResult(5, List.of(3), 2));

        String result = enemyBehaviorService.executeEnemyTurn(10L, 1L);

        assertTrue(result.contains("is defeated!"));
        assertEquals(0, player.getHitPoints());
        assertTrue(player.getIsDefeated());
        assertTrue(player.getPlayerCharacter().getIsUnconscious());
        verify(combatantRepository).save(player);
    }

    @Test
    void testNoValidTargets() {
        player.setIsDefeated(true);
        when(combatantRepository.findById(1L)).thenReturn(Optional.of(enemy));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(any())).thenReturn(List.of(player));

        String result = enemyBehaviorService.executeEnemyTurn(10L, 1L);

        assertTrue(result.contains("finds no valid targets"));
        verify(diceService, never()).roll(anyInt(), anyInt(), anyInt());
    }
}
