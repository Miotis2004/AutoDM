package com.autodm.server.service.narrative;

import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.CampaignEventType;
import com.autodm.server.service.dm.PlayerAction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates structured narrative responses from game state and events.
 */
@Service
public class NarrativeTemplateService {

    public NarrativeMessage generatePlayerActionNarrative(PlayerAction action) {
        String actionType = action.getActionType() != null ? action.getActionType().name() : "UNKNOWN";
        String description = action.getDescription() != null ? action.getDescription() : "took an action";

        String text = String.format("Character %d performed %s: %s",
                action.getCharacterId(), actionType, description);

        return new NarrativeMessage(NarrativeCategory.PLAYER_ACTION, text);
    }

    public NarrativeMessage generateSystemEventNarrative(String description) {
        return new NarrativeMessage(NarrativeCategory.SYSTEM_EVENT, description);
    }

    public NarrativeMessage generateDmNarration(String text) {
        return new NarrativeMessage(NarrativeCategory.DM_NARRATION, text);
    }

    public NarrativeMessage generateDiceResultNarrative(String text) {
        return new NarrativeMessage(NarrativeCategory.DICE_RESULT, text);
    }

    public NarrativeMessage generateCombatEventNarrative(String text) {
        return new NarrativeMessage(NarrativeCategory.COMBAT_EVENT, text);
    }

    public NarrativeMessage generateFromCampaignEvent(CampaignEvent event) {
        NarrativeCategory category = mapEventTypeToCategory(event.getEventType());
        return new NarrativeMessage(category, event.getDescription());
    }

    private NarrativeCategory mapEventTypeToCategory(CampaignEventType type) {
        if (type == null) {
            return NarrativeCategory.SYSTEM_EVENT;
        }

        switch (type) {
            case COMBAT:
            case DAMAGE:
                return NarrativeCategory.COMBAT_EVENT;
            case SESSION_START:
            case SESSION_END:
                return NarrativeCategory.SYSTEM_EVENT;
            default:
                return NarrativeCategory.DM_NARRATION;
        }
    }
}
