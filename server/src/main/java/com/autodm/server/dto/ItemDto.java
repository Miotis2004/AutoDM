package com.autodm.server.dto;

import com.autodm.server.model.ItemCategory;

public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private ItemCategory category;
    private Integer quantity;
    private Integer value;
    private Boolean isEquipped;
    private Long ownerCharacterId;
    private Long campaignId;

    public ItemDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Boolean getIsEquipped() {
        return isEquipped;
    }

    public void setIsEquipped(Boolean isEquipped) {
        this.isEquipped = isEquipped;
    }

    public Long getOwnerCharacterId() {
        return ownerCharacterId;
    }

    public void setOwnerCharacterId(Long ownerCharacterId) {
        this.ownerCharacterId = ownerCharacterId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }
}
