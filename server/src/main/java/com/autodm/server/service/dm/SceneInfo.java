package com.autodm.server.service.dm;

import java.util.List;

/**
 * Represents the current scene information presented to the player.
 */
public class SceneInfo {

    private String title;
    private String narrative;
    private Long currentLocationId;
    private List<Long> involvedCharacterIds;
    private List<String> availableActions;
    private Long encounterId;
    private String status;

    public SceneInfo() {
    }

    public SceneInfo(String title, String narrative, Long currentLocationId, List<Long> involvedCharacterIds, List<String> availableActions, Long encounterId, String status) {
        this.title = title;
        this.narrative = narrative;
        this.currentLocationId = currentLocationId;
        this.involvedCharacterIds = involvedCharacterIds;
        this.availableActions = availableActions;
        this.encounterId = encounterId;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public Long getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocationId(Long currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public List<Long> getInvolvedCharacterIds() {
        return involvedCharacterIds;
    }

    public void setInvolvedCharacterIds(List<Long> involvedCharacterIds) {
        this.involvedCharacterIds = involvedCharacterIds;
    }

    public List<String> getAvailableActions() {
        return availableActions;
    }

    public void setAvailableActions(List<String> availableActions) {
        this.availableActions = availableActions;
    }

    public Long getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Long encounterId) {
        this.encounterId = encounterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
