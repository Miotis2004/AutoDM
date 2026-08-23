package com.autodm.server.dto;

public class CharacterResourceDto {
    private Long id;
    private Long playerCharacterId;
    private String name;
    private String resourceType;
    private Integer currentValue;
    private Integer maximumValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerCharacterId() {
        return playerCharacterId;
    }

    public void setPlayerCharacterId(Long playerCharacterId) {
        this.playerCharacterId = playerCharacterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(Integer maximumValue) {
        this.maximumValue = maximumValue;
    }
}
