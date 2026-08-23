package com.autodm.server.dto;

import com.autodm.server.model.LocationType;
import java.util.Set;

public class LocationDto {
    private Long id;
    private Long campaignId;
    private String name;
    private LocationType type;
    private String description;
    private Boolean isDiscovered;
    private Long parentLocationId;
    private Set<Long> connectedLocationIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDiscovered() {
        return isDiscovered;
    }

    public void setIsDiscovered(Boolean isDiscovered) {
        this.isDiscovered = isDiscovered;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Long parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

    public Set<Long> getConnectedLocationIds() {
        return connectedLocationIds;
    }

    public void setConnectedLocationIds(Set<Long> connectedLocationIds) {
        this.connectedLocationIds = connectedLocationIds;
    }
}
