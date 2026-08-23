package com.autodm.server.dto;

import com.autodm.server.model.QuestStatus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestDto {

    private Long id;
    private Long campaignId;
    private String title;
    private String description;
    private QuestStatus status;
    private Long questGiverId;
    private Set<Long> relatedLocationIds = new HashSet<>();
    private String rewards;
    private String notes;
    private List<ObjectiveDto> objectives = new ArrayList<>();

    public QuestDto() {
    }

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

    public QuestStatus getStatus() {
        return status;
    }

    public void setStatus(QuestStatus status) {
        this.status = status;
    }

    public Long getQuestGiverId() {
        return questGiverId;
    }

    public void setQuestGiverId(Long questGiverId) {
        this.questGiverId = questGiverId;
    }

    public Set<Long> getRelatedLocationIds() {
        return relatedLocationIds;
    }

    public void setRelatedLocationIds(Set<Long> relatedLocationIds) {
        this.relatedLocationIds = relatedLocationIds;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ObjectiveDto> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<ObjectiveDto> objectives) {
        this.objectives = objectives;
    }
}
