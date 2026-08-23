package com.autodm.server.service.dm;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.repository.CampaignEventRepository;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.SceneRepository;
import com.autodm.server.service.narrative.NarrativeCategory;
import com.autodm.server.service.EventService;
import com.autodm.server.service.narrative.NarrativeMessage;
import com.autodm.server.service.narrative.NarrativeTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeterministicDungeonMasterEngineTest {

    private CampaignRepository campaignRepository;
    private CampaignEventRepository campaignEventRepository;
    private SceneRepository sceneRepository;
    private NarrativeTemplateService narrativeTemplateService;
    private EventService eventService;
    private DeterministicDungeonMasterEngine engine;

    @BeforeEach
    void setUp() {
        campaignRepository = mock(CampaignRepository.class);
        campaignEventRepository = mock(CampaignEventRepository.class);
        sceneRepository = mock(SceneRepository.class);
        narrativeTemplateService = new NarrativeTemplateService();
        eventService = mock(EventService.class);
        engine = new DeterministicDungeonMasterEngine(campaignRepository, campaignEventRepository, sceneRepository, narrativeTemplateService, eventService);
        when(sceneRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(eventService.logEvent(any(), any(), anyString())).thenAnswer(invocation ->
            new CampaignEvent((Campaign) invocation.getArgument(0),
                              (com.autodm.server.model.CampaignEventType) invocation.getArgument(1),
                              (String) invocation.getArgument(2))
        );
    }

    @Test
    void testHandleAction_RecordsEventAndReturnsResponse() {
        // Arrange
        Long campaignId = 1L;
        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setTitle("Test Campaign");

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        PlayerAction action = new PlayerAction();
        action.setCharacterId(10L);
        action.setActionType(PlayerActionType.ATTACK);
        action.setDescription("Attack the goblin with sword");

        // Act
        ActionResponse response = engine.handleAction(campaignId, action);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getNarrative().contains("Attack the goblin with sword"));

        assertNotNull(response.getNarrativeLog());
        assertEquals(3, response.getNarrativeLog().size());
        assertEquals(NarrativeCategory.PLAYER_ACTION, response.getNarrativeLog().get(0).getCategory());
        assertEquals(NarrativeCategory.DM_NARRATION, response.getNarrativeLog().get(1).getCategory());
        assertEquals(NarrativeCategory.COMBAT_EVENT, response.getNarrativeLog().get(2).getCategory()); // COMBAT event maps to COMBAT_EVENT

        ArgumentCaptor<com.autodm.server.model.CampaignEventType> typeCaptor = ArgumentCaptor.forClass(com.autodm.server.model.CampaignEventType.class);
        verify(eventService, times(1)).logEvent(eq(campaign), typeCaptor.capture(), anyString());

        com.autodm.server.model.CampaignEventType savedType = typeCaptor.getValue();
        assertEquals(com.autodm.server.model.CampaignEventType.COMBAT, savedType);

        SceneInfo scene = response.getUpdatedScene();
        assertNotNull(scene);
        assertEquals("Campaign: Test Campaign", scene.getTitle());
    }

    @Test
    void testHandleAction_RejectsTravelInEncounter() {
        // Arrange
        Long campaignId = 1L;
        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setTitle("Test Campaign");

        com.autodm.server.model.Scene scene = new com.autodm.server.model.Scene();
        scene.setCampaign(campaign);
        scene.setStatus(com.autodm.server.model.SceneStatus.ACTIVE);

        com.autodm.server.model.Encounter encounter = new com.autodm.server.model.Encounter();
        encounter.setId(100L);
        scene.setActiveEncounter(encounter);

        campaign.setCurrentScene(scene);

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        PlayerAction action = new PlayerAction();
        action.setCharacterId(10L);
        action.setActionType(PlayerActionType.TRAVEL);
        action.setDescription("Travel away");

        // Act
        ActionResponse response = engine.handleAction(campaignId, action);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getNarrative().contains("You cannot travel while in an active encounter."));
        assertEquals(2, response.getNarrativeLog().size());
    }

    @Test
    void testHandleAction_RejectsRestInEncounter() {
        // Arrange
        Long campaignId = 1L;
        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setTitle("Test Campaign");

        com.autodm.server.model.Scene scene = new com.autodm.server.model.Scene();
        scene.setCampaign(campaign);
        scene.setStatus(com.autodm.server.model.SceneStatus.ACTIVE);

        com.autodm.server.model.Encounter encounter = new com.autodm.server.model.Encounter();
        encounter.setId(100L);
        scene.setActiveEncounter(encounter);

        campaign.setCurrentScene(scene);

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        PlayerAction action = new PlayerAction();
        action.setCharacterId(10L);
        action.setActionType(PlayerActionType.REST);
        action.setDescription("Take a short rest");

        // Act
        ActionResponse response = engine.handleAction(campaignId, action);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getNarrative().contains("You cannot rest while in an active encounter."));
        assertEquals(2, response.getNarrativeLog().size());
    }

    @Test
    void testHandleAction_RejectsUnknown() {
        // Arrange
        Long campaignId = 1L;
        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setTitle("Test Campaign");

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        PlayerAction action = new PlayerAction();
        action.setCharacterId(10L);
        action.setActionType(PlayerActionType.UNKNOWN);
        action.setDescription("some weird action");

        // Act
        ActionResponse response = engine.handleAction(campaignId, action);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getNarrative().contains("I don't understand that action."));
        assertEquals(2, response.getNarrativeLog().size());
    }

    @Test
    void testGetCurrentScene_ReturnsSceneInfo() {
        // Arrange
        Long campaignId = 2L;
        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setTitle("Another Campaign");

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));

        // Act
        SceneInfo scene = engine.getCurrentScene(campaignId);

        // Assert
        assertNotNull(scene);
        assertEquals("Campaign: Another Campaign", scene.getTitle());
        assertEquals("ACTIVE", scene.getStatus());
        assertFalse(scene.getAvailableActions().isEmpty());
    }

    @Test
    void testGetCurrentScene_CampaignNotFound_ThrowsException() {
        // Arrange
        Long campaignId = 99L;
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> engine.getCurrentScene(campaignId));
    }
}
