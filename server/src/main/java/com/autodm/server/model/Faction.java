package com.autodm.server.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Faction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String disposition;

    @Column
    private Integer reputation;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToMany
    @JoinTable(
        name = "faction_allies",
        joinColumns = @JoinColumn(name = "faction_id"),
        inverseJoinColumns = @JoinColumn(name = "ally_faction_id")
    )
    private Set<Faction> allies = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "faction_enemies",
        joinColumns = @JoinColumn(name = "faction_id"),
        inverseJoinColumns = @JoinColumn(name = "enemy_faction_id")
    )
    private Set<Faction> enemies = new HashSet<>();

    public Faction() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Faction> getAllies() {
        return allies;
    }

    public void setAllies(Set<Faction> allies) {
        this.allies = allies;
    }

    public Set<Faction> getEnemies() {
        return enemies;
    }

    public void setEnemies(Set<Faction> enemies) {
        this.enemies = enemies;
    }
}
