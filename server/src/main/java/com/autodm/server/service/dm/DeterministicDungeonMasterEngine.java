package com.autodm.server.service.dm;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.CampaignEventType;
import com.autodm.server.repository.CampaignEventRepository;
import com.autodm.server.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class DeterministicDungeonMasterEngine implements DungeonMasterEngine {

    private final CampaignRepository campaignRepository;
    private final CampaignEventRepository campaignEventRepository;

    public DeterministicDungeonMasterEngine(CampaignRepository campaignRepository,
                                            CampaignEventRepository campaignEventRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignEventRepository = campaignEventRepository;
    }

    @Override
    @Transactional
    public ActionResponse handleAction(Long campaignId, PlayerAction action) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        // Basic deterministic handling
        // We'll log the action as a generic discovery or system event for now
        String narrative = "You attempted to " + action.getDescription() + ". It was somewhat successful.";

        CampaignEvent event = new CampaignEvent(campaign, CampaignEventType.DISCOVERY,
                "Player character " + action.getCharacterId() + " took action: " + action.getActionType() + " - " + action.getDescription());
        campaignEventRepository.save(event);

        SceneInfo updatedScene = getCurrentScene(campaignId);
        updatedScene.setNarrative(narrative);

        return new ActionResponse(
                true,
                narrative,
                Collections.singletonList("Recorded player action"),
                updatedScene
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SceneInfo getCurrentScene(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        // Mock current scene info based on deterministic logic
        // We could look up the most recent events, active encounter, etc.
        SceneInfo info = new SceneInfo();
        info.setTitle("Campaign: " + campaign.getTitle());
        info.setNarrative("You are in a dimly lit tavern.");
        info.setStatus("IDLE");
        info.setAvailableActions(List.of("Look around", "Talk to barkeep", "Leave"));
        info.setInvolvedCharacterIds(Collections.emptyList());

        return info;
    }
}
