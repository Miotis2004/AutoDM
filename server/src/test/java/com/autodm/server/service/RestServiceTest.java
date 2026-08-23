package com.autodm.server.service;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.CampaignEventType;
import com.autodm.server.model.CharacterResource;
import com.autodm.server.model.Combatant;
import com.autodm.server.model.Condition;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CampaignEventRepository;
import com.autodm.server.repository.CharacterResourceRepository;
import com.autodm.server.repository.CombatantRepository;
import com.autodm.server.repository.PlayerCharacterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestServiceTest {

    @Mock
    private PlayerCharacterRepository playerCharacterRepository;

    @Mock
    private ConditionService conditionService;

    @Mock
    private CombatantRepository combatantRepository;

    @Mock
    private CharacterResourceRepository characterResourceRepository;

    @Mock
    private CampaignEventRepository campaignEventRepository;

    @InjectMocks
    private RestService restService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPerformShortRest() {
        Campaign campaign = new Campaign();

        PlayerCharacter pc = new PlayerCharacter();
        pc.setId(1L);
        pc.setHitPoints(5);
        pc.setMaximumHitPoints(20);

        CharacterResource resource = new CharacterResource();
        resource.setId(10L);
        resource.setCurrentValue(5);

        restService.performShortRest(campaign, Arrays.asList(pc), Arrays.asList(100L, 101L), Arrays.asList(resource), 10);

        assertEquals(15, pc.getHitPoints());
        verify(playerCharacterRepository, times(1)).save(pc);
        verify(conditionService, times(1)).deactivateCondition(100L);
        verify(conditionService, times(1)).deactivateCondition(101L);
        verify(characterResourceRepository, times(1)).save(resource);

        ArgumentCaptor<CampaignEvent> eventCaptor = ArgumentCaptor.forClass(CampaignEvent.class);
        verify(campaignEventRepository, times(1)).save(eventCaptor.capture());

        CampaignEvent savedEvent = eventCaptor.getValue();
        assertEquals(campaign, savedEvent.getCampaign());
        assertEquals(CampaignEventType.SHORT_REST, savedEvent.getEventType());
    }

    @Test
    void testPerformLongRest() {
        Campaign campaign = new Campaign();

        PlayerCharacter pc = new PlayerCharacter();
        pc.setId(1L);
        pc.setHitPoints(5);
        pc.setMaximumHitPoints(20);
        pc.setTemporaryHitPoints(10);

        Combatant combatant = new Combatant();
        combatant.setId(2L);

        Condition c1 = new Condition();
        c1.setId(10L);
        c1.setDuration(5);

        Condition c2 = new Condition();
        c2.setId(11L);
        c2.setDuration(null);

        CharacterResource resource1 = new CharacterResource();
        resource1.setId(100L);
        resource1.setCurrentValue(1);
        resource1.setMaximumValue(5);

        CharacterResource resource2 = new CharacterResource();
        resource2.setId(101L);
        resource2.setCurrentValue(10);
        resource2.setMaximumValue(null);

        when(combatantRepository.findByPlayerCharacterId(1L)).thenReturn(Arrays.asList(combatant));
        when(conditionService.getActiveConditions(2L)).thenReturn(Arrays.asList(c1, c2));
        when(characterResourceRepository.findByPlayerCharacterId(1L)).thenReturn(Arrays.asList(resource1, resource2));

        restService.performLongRest(campaign, Arrays.asList(pc));

        assertEquals(20, pc.getHitPoints());
        assertEquals(0, pc.getTemporaryHitPoints());
        verify(playerCharacterRepository, times(1)).save(pc);

        verify(conditionService, times(1)).deactivateCondition(10L);
        verify(conditionService, never()).deactivateCondition(11L);

        assertEquals(5, resource1.getCurrentValue());
        verify(characterResourceRepository, times(1)).save(resource1);
        verify(characterResourceRepository, never()).save(resource2);

        ArgumentCaptor<CampaignEvent> eventCaptor = ArgumentCaptor.forClass(CampaignEvent.class);
        verify(campaignEventRepository, times(1)).save(eventCaptor.capture());

        CampaignEvent savedEvent = eventCaptor.getValue();
        assertEquals(campaign, savedEvent.getCampaign());
        assertEquals(CampaignEventType.LONG_REST, savedEvent.getEventType());
    }
}
