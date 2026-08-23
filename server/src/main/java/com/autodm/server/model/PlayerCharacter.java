package com.autodm.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class PlayerCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @Column(nullable = false)
    private String name;

    private String ancestry;

    @Column(name = "character_class")
    private String characterClass;

    private Integer level = 1;

    private String background;
    private String alignment;

    private Integer hitPoints = 10;
    private Integer maximumHitPoints = 10;
    private Integer armorClass = 10;
    private Integer movement = 30;

    // Ability scores
    private Integer strength = 10;
    private Integer dexterity = 10;
    private Integer constitution = 10;
    private Integer intelligence = 10;
    private Integer wisdom = 10;
    private Integer charisma = 10;

    // We can store proficiencies/saving throws/skills as simple strings or JSON for now,
    // or as discrete fields. Using simple comma-separated text or JSON blocks for flexibility.
    @Column(columnDefinition = "TEXT")
    private String savingThrowProficiencies;

    @Column(columnDefinition = "TEXT")
    private String skillProficiencies;

    @Column(columnDefinition = "TEXT")
    private String generalProficiencies;

    public PlayerCharacter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
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
}
