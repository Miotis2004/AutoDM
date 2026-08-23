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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestService {

    private final PlayerCharacterRepository playerCharacterRepository;
    private final ConditionService conditionService;
    private final CombatantRepository combatantRepository;
    private final CharacterResourceRepository characterResourceRepository;
    private final CampaignEventRepository campaignEventRepository;

    public RestService(PlayerCharacterRepository playerCharacterRepository,
                       ConditionService conditionService,
                       CombatantRepository combatantRepository,
                       CharacterResourceRepository characterResourceRepository,
                       CampaignEventRepository campaignEventRepository) {
        this.playerCharacterRepository = playerCharacterRepository;
        this.conditionService = conditionService;
        this.combatantRepository = combatantRepository;
        this.characterResourceRepository = characterResourceRepository;
        this.campaignEventRepository = campaignEventRepository;
    }

    @Transactional
    public void performShortRest(Campaign campaign, List<PlayerCharacter> characters,
                                 List<Long> conditionsToClear,
                                 List<CharacterResource> resourcesToRestore,
                                 int healingAmount) {

        for (PlayerCharacter pc : characters) {
            if (healingAmount > 0) {
                int newHp = pc.getHitPoints() + healingAmount;
                if (newHp > pc.getMaximumHitPoints()) {
                    newHp = pc.getMaximumHitPoints();
                }
                pc.setHitPoints(newHp);
            }
            playerCharacterRepository.save(pc);
        }

        if (conditionsToClear != null) {
            for (Long conditionId : conditionsToClear) {
                conditionService.deactivateCondition(conditionId);
            }
        }

        if (resourcesToRestore != null) {
            for (CharacterResource resource : resourcesToRestore) {
                characterResourceRepository.save(resource);
            }
        }

        CampaignEvent event = new CampaignEvent(campaign, CampaignEventType.SHORT_REST, "The party took a short rest.");
        campaignEventRepository.save(event);
    }

    @Transactional
    public void performLongRest(Campaign campaign, List<PlayerCharacter> characters) {
        for (PlayerCharacter pc : characters) {
            pc.setHitPoints(pc.getMaximumHitPoints());
            pc.setTemporaryHitPoints(0);
            playerCharacterRepository.save(pc);

            List<Combatant> combatants = combatantRepository.findByPlayerCharacterId(pc.getId());
            for (Combatant combatant : combatants) {
                List<Condition> activeConditions = conditionService.getActiveConditions(combatant.getId());
                for (Condition condition : activeConditions) {
                    if (condition.getDuration() != null) {
                        conditionService.deactivateCondition(condition.getId());
                    }
                }
            }

            List<CharacterResource> resources = characterResourceRepository.findByPlayerCharacterId(pc.getId());
            for (CharacterResource resource : resources) {
                if (resource.getMaximumValue() != null) {
                    resource.setCurrentValue(resource.getMaximumValue());
                    characterResourceRepository.save(resource);
                }
            }
        }

        CampaignEvent event = new CampaignEvent(campaign, CampaignEventType.LONG_REST, "The party took a long rest.");
        campaignEventRepository.save(event);
    }
}
