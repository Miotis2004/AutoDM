package com.autodm.server.dto;

import com.autodm.server.model.CampaignEventType;
import java.time.LocalDateTime;

public class CampaignEventDto {
    private Long id;
    private CampaignEventType eventType;
    private LocalDateTime timestamp;
    private Long campaignId;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CampaignEventType getEventType() {
        return eventType;
    }

    public void setEventType(CampaignEventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
