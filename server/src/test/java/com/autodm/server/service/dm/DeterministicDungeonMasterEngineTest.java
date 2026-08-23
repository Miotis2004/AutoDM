package com.autodm.server.service.dm;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.repository.CampaignEventRepository;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.SceneRepository;
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
    private DeterministicDungeonMasterEngine engine;

    @BeforeEach
    void setUp() {
        campaignRepository = mock(CampaignRepository.class);
        campaignEventRepository = mock(CampaignEventRepository.class);
        sceneRepository = mock(SceneRepository.class);
        engine = new DeterministicDungeonMasterEngine(campaignRepository, campaignEventRepository, sceneRepository);
        when(sceneRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
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
        action.setActionType("ATTACK");
        action.setDescription("Attack the goblin with sword");

        // Act
        ActionResponse response = engine.handleAction(campaignId, action);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getNarrative().contains("Attack the goblin with sword"));

        ArgumentCaptor<CampaignEvent> eventCaptor = ArgumentCaptor.forClass(CampaignEvent.class);
        verify(campaignEventRepository, times(1)).save(eventCaptor.capture());

        CampaignEvent savedEvent = eventCaptor.getValue();
        assertEquals(campaign, savedEvent.getCampaign());
        assertTrue(savedEvent.getDescription().contains("Attack the goblin with sword"));

        SceneInfo scene = response.getUpdatedScene();
        assertNotNull(scene);
        assertEquals("Campaign: Test Campaign", scene.getTitle());
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
