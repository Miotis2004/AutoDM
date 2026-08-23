package com.autodm.server.service.narrative;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.CampaignEventType;
import com.autodm.server.service.dm.PlayerAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NarrativeTemplateServiceTest {

    private NarrativeTemplateService service;

    @BeforeEach
    void setUp() {
        service = new NarrativeTemplateService();
    }

    @Test
    void testGeneratePlayerActionNarrative() {
        PlayerAction action = new PlayerAction();
        action.setCharacterId(1L);
        action.setActionType(com.autodm.server.service.dm.PlayerActionType.ATTACK);
        action.setDescription("Attack the goblin with sword");

        NarrativeMessage message = service.generatePlayerActionNarrative(action);

        assertEquals(NarrativeCategory.PLAYER_ACTION, message.getCategory());
        assertEquals("Character 1 performed ATTACK: Attack the goblin with sword", message.getText());
    }

    @Test
    void testGenerateDmNarration() {
        NarrativeMessage message = service.generateDmNarration("The goblin parries your attack.");

        assertEquals(NarrativeCategory.DM_NARRATION, message.getCategory());
        assertEquals("The goblin parries your attack.", message.getText());
    }

    @Test
    void testGenerateFromCampaignEvent() {
        Campaign campaign = new Campaign();
        CampaignEvent event = new CampaignEvent(campaign, CampaignEventType.COMBAT, "Goblin takes 5 damage");

        NarrativeMessage message = service.generateFromCampaignEvent(event);

        assertEquals(NarrativeCategory.COMBAT_EVENT, message.getCategory());
        assertEquals("Goblin takes 5 damage", message.getText());
    }
}
