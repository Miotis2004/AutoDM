package com.autodm.server.dto;

import java.util.HashSet;
import java.util.Set;

public class FactionDto {

    private Long id;
    private Long campaignId;
    private String name;
    private String description;
    private String disposition;
    private Integer reputation;
    private String notes;
    private Set<Long> allyIds = new HashSet<>();
    private Set<Long> enemyIds = new HashSet<>();

    public FactionDto() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Long> getAllyIds() {
        return allyIds;
    }

    public void setAllyIds(Set<Long> allyIds) {
        this.allyIds = allyIds;
    }

    public Set<Long> getEnemyIds() {
        return enemyIds;
    }

    public void setEnemyIds(Set<Long> enemyIds) {
        this.enemyIds = enemyIds;
    }
}
