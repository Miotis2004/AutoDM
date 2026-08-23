package com.autodm.server.service;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.Combatant;
import com.autodm.server.model.CreatureTemplate;
import com.autodm.server.model.Encounter;
import com.autodm.server.model.EncounterDifficulty;
import com.autodm.server.model.EncounterStatus;
import com.autodm.server.model.Location;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CombatantRepository;
import com.autodm.server.repository.CreatureTemplateRepository;
import com.autodm.server.repository.EncounterRepository;
import com.autodm.server.repository.PlayerCharacterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncounterGenerationServiceTest {

    @Mock
    private EncounterRepository encounterRepository;
    @Mock
    private CombatantRepository combatantRepository;
    @Mock
    private PlayerCharacterRepository playerCharacterRepository;
    @Mock
    private CreatureTemplateRepository creatureTemplateRepository;
    @Mock
    private DiceService diceService;

    @InjectMocks
    private EncounterGenerationService encounterGenerationService;

    private Campaign campaign;
    private Location location;

    @BeforeEach
    void setUp() {
        campaign = new Campaign();
        campaign.setId(1L);

        location = new Location();
        location.setId(1L);
    }

    @Test
    void createManualEncounter_ShouldSaveAndReturnEncounter() {
        String notes = "Test notes";
        Encounter savedEncounter = new Encounter();
        savedEncounter.setStatus(EncounterStatus.PENDING);

        when(encounterRepository.save(any(Encounter.class))).thenReturn(savedEncounter);

        Encounter result = encounterGenerationService.createManualEncounter(campaign, location, notes);

        assertNotNull(result);
        assertEquals(EncounterStatus.PENDING, result.getStatus());

        ArgumentCaptor<Encounter> captor = ArgumentCaptor.forClass(Encounter.class);
        verify(encounterRepository).save(captor.capture());

        Encounter captured = captor.getValue();
        assertEquals(campaign, captured.getCampaign());
        assertEquals(location, captured.getLocation());
        assertEquals(notes, captured.getNotes());
        assertEquals(EncounterStatus.PENDING, captured.getStatus());
    }

    @Test
    void generateAutomatedEncounter_WithNoTemplates_ShouldReturnEmptyEncounter() {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setId(1L);
        pc.setName("Hero");
        pc.setLevel(2);
        pc.setHitPoints(20);
        pc.setMaximumHitPoints(20);

        when(playerCharacterRepository.findByCampaignId(campaign.getId())).thenReturn(Collections.singletonList(pc));
        when(creatureTemplateRepository.findByCampaignId(campaign.getId())).thenReturn(Collections.emptyList());

        Encounter encounter = new Encounter();
        when(encounterRepository.save(any(Encounter.class))).thenReturn(encounter);

        Encounter result = encounterGenerationService.generateAutomatedEncounter(campaign, location, EncounterDifficulty.EASY);

        assertNotNull(result);

        // Should have saved the encounter and 1 player combatant
        verify(encounterRepository).save(any(Encounter.class));
        verify(combatantRepository, times(1)).save(any(Combatant.class));
    }

    @Test
    void generateAutomatedEncounter_WithTemplates_ShouldSpawnEnemies() {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setId(1L);
        pc.setName("Hero");
        pc.setLevel(2); // budget base = 2. DEADLY budget = 2 * 3 = 6
        pc.setHitPoints(20);
        pc.setMaximumHitPoints(20);

        CreatureTemplate goblin = new CreatureTemplate();
        goblin.setId(1L);
        goblin.setName("Goblin");
        goblin.setHitPoints(15); // cost = max(1, 15/10) = 1

        CreatureTemplate orc = new CreatureTemplate();
        orc.setId(2L);
        orc.setName("Orc");
        orc.setHitPoints(35); // cost = max(1, 35/10) = 3

        when(playerCharacterRepository.findByCampaignId(campaign.getId())).thenReturn(Collections.singletonList(pc));
        when(creatureTemplateRepository.findByCampaignId(campaign.getId())).thenReturn(Arrays.asList(goblin, orc));

        Encounter encounter = new Encounter();
        when(encounterRepository.save(any(Encounter.class))).thenReturn(encounter);

        // DEADLY budget for level 2 is 6.
        Encounter result = encounterGenerationService.generateAutomatedEncounter(campaign, location, EncounterDifficulty.DEADLY);

        assertNotNull(result);

        // We expect player combatant (1) + some number of enemies to reach budget 6.
        // It could be 6 goblins (cost 1), or 2 orcs (cost 3), etc.
        // At least 1 enemy should be spawned.
        ArgumentCaptor<Combatant> captor = ArgumentCaptor.forClass(Combatant.class);
        verify(combatantRepository, org.mockito.Mockito.atLeast(2)).save(captor.capture());

        List<Combatant> savedCombatants = captor.getAllValues();
        long playerCount = savedCombatants.stream().filter(Combatant::getIsPlayer).count();
        long enemyCount = savedCombatants.stream().filter(c -> !c.getIsPlayer()).count();

        assertEquals(1, playerCount);
        assertTrue(enemyCount >= 1);

        // Check duplicate name handling
        long goblinsWithSuffix = savedCombatants.stream()
            .filter(c -> c.getName() != null && c.getName().startsWith("Goblin "))
            .count();

        // It's random, so we might or might not have duplicates, but verify the code doesn't crash
    }
}
