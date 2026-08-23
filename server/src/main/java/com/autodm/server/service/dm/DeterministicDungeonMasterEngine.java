package com.autodm.server.service.dm;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.CampaignEventType;
import com.autodm.server.model.Scene;
import com.autodm.server.model.SceneStatus;
import com.autodm.server.repository.CampaignEventRepository;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.SceneRepository;
import com.autodm.server.service.narrative.NarrativeMessage;
import com.autodm.server.service.narrative.NarrativeTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeterministicDungeonMasterEngine implements DungeonMasterEngine {

    private final CampaignRepository campaignRepository;
    private final CampaignEventRepository campaignEventRepository;
    private final SceneRepository sceneRepository;
    private final NarrativeTemplateService narrativeTemplateService;

    public DeterministicDungeonMasterEngine(CampaignRepository campaignRepository,
                                            CampaignEventRepository campaignEventRepository,
                                            SceneRepository sceneRepository,
                                            NarrativeTemplateService narrativeTemplateService) {
        this.campaignRepository = campaignRepository;
        this.campaignEventRepository = campaignEventRepository;
        this.sceneRepository = sceneRepository;
        this.narrativeTemplateService = narrativeTemplateService;
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

        List<NarrativeMessage> narrativeLog = new ArrayList<>();
        narrativeLog.add(narrativeTemplateService.generatePlayerActionNarrative(action));
        narrativeLog.add(narrativeTemplateService.generateDmNarration(narrative));
        narrativeLog.add(narrativeTemplateService.generateFromCampaignEvent(event));

        return new ActionResponse(
                true,
                narrative,
                narrativeLog,
                Collections.singletonList("Recorded player action"),
                updatedScene
        );
    }

    @Override
    @Transactional
    public SceneInfo getCurrentScene(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + campaignId));

        Scene currentScene = campaign.getCurrentScene();
        if (currentScene == null) {
            currentScene = new Scene();
            currentScene.setCampaign(campaign);
            currentScene.setTitle("Campaign: " + campaign.getTitle());
            currentScene.setNarrative("You are in a dimly lit tavern.");
            currentScene.setStatus(SceneStatus.ACTIVE);
            currentScene = sceneRepository.save(currentScene);
            campaign.setCurrentScene(currentScene);
            campaignRepository.save(campaign);
        }

        SceneInfo info = new SceneInfo();
        info.setTitle(currentScene.getTitle());
        info.setNarrative(currentScene.getNarrative());
        info.setStatus(currentScene.getStatus().name());
        info.setAvailableActions(List.of("Look around", "Talk to barkeep", "Leave"));

        List<Long> characterIds = currentScene.getInvolvedPlayerCharacters().stream()
                .map(pc -> pc.getId())
                .collect(Collectors.toList());
        info.setInvolvedCharacterIds(characterIds);

        if (currentScene.getCurrentLocation() != null) {
            info.setCurrentLocationId(currentScene.getCurrentLocation().getId());
        }

        if (currentScene.getActiveEncounter() != null) {
            info.setEncounterId(currentScene.getActiveEncounter().getId());
        }

        return info;
    }
}
