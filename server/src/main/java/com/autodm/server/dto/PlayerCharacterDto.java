package com.autodm.server.dto;

public class PlayerCharacterDto {
    private Long id;
    private Long campaignId;
    private String name;
    private String ancestry;
    private String characterClass;
    private Integer level;
    private String background;
    private String alignment;

    private Integer hitPoints;
    private Integer maximumHitPoints;
    private Integer temporaryHitPoints;

    private Boolean isUnconscious;
    private Boolean isDead;

    private Integer armorClass;
    private Integer movement;

    private Integer strength;
    private Integer dexterity;
    private Integer constitution;
    private Integer intelligence;
    private Integer wisdom;
    private Integer charisma;

    private String savingThrowProficiencies;
    private String skillProficiencies;
    private String generalProficiencies;
    private String conditions;

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

    public String getAncestry() {
        return ancestry;
    }

    public void setAncestry(String ancestry) {
        this.ancestry = ancestry;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public Integer getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(Integer hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Integer getMaximumHitPoints() {
        return maximumHitPoints;
    }

    public void setMaximumHitPoints(Integer maximumHitPoints) {
        this.maximumHitPoints = maximumHitPoints;
    }

    public Integer getTemporaryHitPoints() {
        return temporaryHitPoints;
    }

    public void setTemporaryHitPoints(Integer temporaryHitPoints) {
        this.temporaryHitPoints = temporaryHitPoints;
    }

    public Boolean getIsUnconscious() {
        return isUnconscious;
    }

    public void setIsUnconscious(Boolean unconscious) {
        isUnconscious = unconscious;
    }

    public Boolean getIsDead() {
        return isDead;
    }

    public void setIsDead(Boolean dead) {
        isDead = dead;
    }

    public Integer getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(Integer armorClass) {
        this.armorClass = armorClass;
    }

    public Integer getMovement() {
        return movement;
    }

    public void setMovement(Integer movement) {
        this.movement = movement;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getDexterity() {
        return dexterity;
    }

    public void setDexterity(Integer dexterity) {
        this.dexterity = dexterity;
    }

    public Integer getConstitution() {
        return constitution;
    }

    public void setConstitution(Integer constitution) {
        this.constitution = constitution;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getWisdom() {
        return wisdom;
    }

    public void setWisdom(Integer wisdom) {
        this.wisdom = wisdom;
    }

    public Integer getCharisma() {
        return charisma;
    }

    public void setCharisma(Integer charisma) {
        this.charisma = charisma;
    }

    public String getSavingThrowProficiencies() {
        return savingThrowProficiencies;
    }

    public void setSavingThrowProficiencies(String savingThrowProficiencies) {
        this.savingThrowProficiencies = savingThrowProficiencies;
    }

    public String getSkillProficiencies() {
        return skillProficiencies;
    }

    public void setSkillProficiencies(String skillProficiencies) {
        this.skillProficiencies = skillProficiencies;
    }

    public String getGeneralProficiencies() {
        return generalProficiencies;
    }

    public void setGeneralProficiencies(String generalProficiencies) {
        this.generalProficiencies = generalProficiencies;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
