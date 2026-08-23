package com.autodm.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Combatant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer hitPoints;

    @Column(nullable = false)
    private Integer maxHitPoints;

    private Integer initiative;

    @Column(nullable = false)
    private Boolean isPlayer;

    @Column(nullable = false)
    private Boolean isDefeated;

    // Optional references to the original entities
    @ManyToOne
    @JoinColumn(name = "player_character_id")
    private PlayerCharacter playerCharacter;

    @ManyToOne
    @JoinColumn(name = "npc_id")
    private Npc npc;

    @ManyToOne
    @JoinColumn(name = "creature_template_id")
    private CreatureTemplate template;

    public Combatant() {
        this.isPlayer = false;
        this.isDefeated = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(Integer hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Integer getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(Integer maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public Integer getInitiative() {
        return initiative;
    }

    public void setInitiative(Integer initiative) {
        this.initiative = initiative;
    }

    public Boolean getIsPlayer() {
        return isPlayer;
    }

    public void setIsPlayer(Boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    public Boolean getIsDefeated() {
        return isDefeated;
    }

    public void setIsDefeated(Boolean isDefeated) {
        this.isDefeated = isDefeated;
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(PlayerCharacter playerCharacter) {
        this.playerCharacter = playerCharacter;
    }

    public Npc getNpc() {
        return npc;
    }

    public void setNpc(Npc npc) {
        this.npc = npc;
    }

    public CreatureTemplate getTemplate() {
        return template;
    }

    public void setTemplate(CreatureTemplate template) {
        this.template = template;
    }
}
