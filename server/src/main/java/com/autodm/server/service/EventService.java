package com.autodm.server.service;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.CampaignEventType;
import com.autodm.server.repository.CampaignEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final CampaignEventRepository campaignEventRepository;

    public EventService(CampaignEventRepository campaignEventRepository) {
        this.campaignEventRepository = campaignEventRepository;
    }

    @Transactional
    public CampaignEvent logEvent(Campaign campaign, CampaignEventType eventType, String description) {
        CampaignEvent event = new CampaignEvent(campaign, eventType, description);
        return campaignEventRepository.save(event);
    }

    @Transactional
    public CampaignEvent logSessionStart(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.SESSION_START, description);
    }

    @Transactional
    public CampaignEvent logSessionEnd(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.SESSION_END, description);
    }

    @Transactional
    public CampaignEvent logLocationEntry(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.LOCATION_ENTRY, description);
    }

    @Transactional
    public CampaignEvent logDiscovery(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.DISCOVERY, description);
    }

    @Transactional
    public CampaignEvent logCombat(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.COMBAT, description);
    }

    @Transactional
    public CampaignEvent logDamage(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.DAMAGE, description);
    }

    @Transactional
    public CampaignEvent logItemAcquisition(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.ITEM_ACQUISITION, description);
    }

    @Transactional
    public CampaignEvent logQuestChange(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.QUEST_CHANGE, description);
    }

    @Transactional
    public CampaignEvent logRelationshipChange(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.RELATIONSHIP_CHANGE, description);
    }

    @Transactional
    public CampaignEvent logShortRest(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.SHORT_REST, description);
    }

    @Transactional
    public CampaignEvent logLongRest(Campaign campaign, String description) {
        return logEvent(campaign, CampaignEventType.LONG_REST, description);
    }
}
