package com.autodm.server.service.dm;

import java.util.Map;

/**
 * Represents an action taken by a player.
 */
public class PlayerAction {

    private Long characterId;
    private String actionType;
    private String description;
    private Map<String, Object> parameters;

    public PlayerAction() {
    }

    public PlayerAction(Long characterId, String actionType, String description, Map<String, Object> parameters) {
        this.characterId = characterId;
        this.actionType = actionType;
        this.description = description;
        this.parameters = parameters;
    }

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
