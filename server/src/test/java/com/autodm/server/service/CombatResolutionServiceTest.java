package com.autodm.server.service;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.Combatant;
import com.autodm.server.model.Encounter;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CampaignEventRepository;
import com.autodm.server.repository.CombatantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CombatResolutionServiceTest {

    @Mock
    private DiceService diceService;

    @Mock
    private CombatantRepository combatantRepository;

    @Mock
    private CampaignEventRepository campaignEventRepository;

    @InjectMocks
    private CombatResolutionService combatResolutionService;

    private Combatant attacker;
    private Combatant target;

    @BeforeEach
    void setUp() {
        Campaign campaign = new Campaign();
        campaign.setId(1L);

        Encounter encounter = new Encounter();
        encounter.setCampaign(campaign);

        attacker = new Combatant();
        attacker.setId(1L);
        attacker.setName("Goblin");
        attacker.setEncounter(encounter);

        target = new Combatant();
        target.setId(2L);
        target.setName("Fighter");
        target.setIsPlayer(true);
        target.setHitPoints(10);
        target.setEncounter(encounter);

        PlayerCharacter pc = new PlayerCharacter();
        pc.setArmorClass(15);
        target.setPlayerCharacter(pc);
    }

    @Test
    void testResolveAttackHit() {
        when(diceService.roll(1, 20, 4)).thenReturn(new RollResult(16, List.of(12), 4));
        when(diceService.roll("1d6+2")).thenReturn(new RollResult(5, List.of(3), 2));

        AttackResult result = combatResolutionService.resolveAttack(attacker, target, 4, "1d6+2");

        assertTrue(result.isHit());
        assertEquals(16, result.getAttackTotal());
        assertEquals(5, result.getDamageApplied());
        assertFalse(result.isTargetDefeated());
        assertTrue(result.getDescription().contains("Hit!"));
        assertEquals(5, target.getHitPoints());

        verify(combatantRepository).save(target);
        verify(campaignEventRepository, times(1)).save(any(CampaignEvent.class));
    }

    @Test
    void testResolveAttackMiss() {
        when(diceService.roll(1, 20, 4)).thenReturn(new RollResult(12, List.of(8), 4));

        AttackResult result = combatResolutionService.resolveAttack(attacker, target, 4, "1d6+2");

        assertFalse(result.isHit());
        assertEquals(12, result.getAttackTotal());
        assertEquals(0, result.getDamageApplied());
        assertFalse(result.isTargetDefeated());
        assertTrue(result.getDescription().contains("Misses"));
        assertEquals(10, target.getHitPoints());

        verify(combatantRepository, never()).save(any());
        verify(campaignEventRepository, times(1)).save(any(CampaignEvent.class));
    }

    @Test
    void testResolveAttackDefeatsTarget() {
        target.setHitPoints(3);

        when(diceService.roll(1, 20, 4)).thenReturn(new RollResult(16, List.of(12), 4));
        when(diceService.roll("1d6+2")).thenReturn(new RollResult(5, List.of(3), 2));

        AttackResult result = combatResolutionService.resolveAttack(attacker, target, 4, "1d6+2");

        assertTrue(result.isHit());
        assertTrue(result.isTargetDefeated());
        assertEquals(0, target.getHitPoints());
        assertTrue(target.getIsDefeated());
        assertTrue(target.getPlayerCharacter().getIsUnconscious());

        verify(combatantRepository).save(target);
        verify(campaignEventRepository, times(1)).save(any(CampaignEvent.class));
    }
}
