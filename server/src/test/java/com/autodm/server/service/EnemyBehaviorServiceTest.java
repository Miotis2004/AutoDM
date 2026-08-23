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
    private CombatResolutionService combatResolutionService;

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
    void testExecuteTurnPerformsAttack() {
        when(combatantRepository.findById(1L)).thenReturn(Optional.of(enemy));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(any())).thenReturn(List.of(player));

        AttackResult attackResult = new AttackResult(true, 16, 5, false, "Goblin attacks Fighter. Roll: 12 + 4 = 16. Hit! Deals 5 damage.");
        when(combatResolutionService.resolveAttack(enemy, player, 4, "1d6+2")).thenReturn(attackResult);

        String result = enemyBehaviorService.executeEnemyTurn(10L, 1L);

        assertEquals("Goblin attacks Fighter. Roll: 12 + 4 = 16. Hit! Deals 5 damage.", result);
        verify(combatResolutionService).resolveAttack(enemy, player, 4, "1d6+2");
    }

    @Test
    void testNoValidTargets() {
        player.setIsDefeated(true);
        when(combatantRepository.findById(1L)).thenReturn(Optional.of(enemy));
        when(combatantRepository.findByEncounterIdOrderByInitiativeDesc(any())).thenReturn(List.of(player));

        String result = enemyBehaviorService.executeEnemyTurn(10L, 1L);

        assertTrue(result.contains("finds no valid targets"));
        verify(combatResolutionService, never()).resolveAttack(any(), any(), anyInt(), anyString());
    }
}
