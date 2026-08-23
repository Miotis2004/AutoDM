package com.autodm.server.dto;

public class NpcDto {

    private Long id;
    private Long campaignId;
    private String name;
    private String description;
    private String role;
    private String disposition;
    private String faction;
    private Long currentLocationId;
    private Boolean isAlive;
    private Boolean isActive;
    private String relationshipToParty;
    private String notes;
    private String combatStatistics;

    public NpcDto() {}

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public Long getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocationId(Long currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getRelationshipToParty() {
        return relationshipToParty;
    }

    public void setRelationshipToParty(String relationshipToParty) {
        this.relationshipToParty = relationshipToParty;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCombatStatistics() {
        return combatStatistics;
    }

    public void setCombatStatistics(String combatStatistics) {
        this.combatStatistics = combatStatistics;
    }
}
