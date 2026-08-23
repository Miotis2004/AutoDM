package com.autodm.server.dto;

import com.autodm.server.model.CampaignStatus;
import java.time.LocalDateTime;

public class CampaignDto {
    private Long id;
    private String title;
    private String description;
    private CampaignStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime lastPlayedDate;
    private String notes;
    private Long currentLocationId;
    private Long currentSceneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastPlayedDate() {
        return lastPlayedDate;
    }

    public void setLastPlayedDate(LocalDateTime lastPlayedDate) {
        this.lastPlayedDate = lastPlayedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocationId(Long currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public Long getCurrentSceneId() {
        return currentSceneId;
    }

    public void setCurrentSceneId(Long currentSceneId) {
        this.currentSceneId = currentSceneId;
    }
}
