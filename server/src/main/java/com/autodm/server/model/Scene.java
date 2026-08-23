package com.autodm.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String narrative;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location currentLocation;

    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private Encounter activeEncounter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SceneStatus status = SceneStatus.ACTIVE;

    @ManyToMany
    @JoinTable(
        name = "scene_player_character",
        joinColumns = @JoinColumn(name = "scene_id"),
        inverseJoinColumns = @JoinColumn(name = "player_character_id")
    )
    private Set<PlayerCharacter> involvedPlayerCharacters = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "scene_npc",
        joinColumns = @JoinColumn(name = "scene_id"),
        inverseJoinColumns = @JoinColumn(name = "npc_id")
    )
    private Set<Npc> involvedNpcs = new HashSet<>();

    public Scene() {}

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

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Encounter getActiveEncounter() {
        return activeEncounter;
    }

    public void setActiveEncounter(Encounter activeEncounter) {
        this.activeEncounter = activeEncounter;
    }

    public SceneStatus getStatus() {
        return status;
    }

    public void setStatus(SceneStatus status) {
        this.status = status;
    }

    public Set<PlayerCharacter> getInvolvedPlayerCharacters() {
        return involvedPlayerCharacters;
    }

    public void setInvolvedPlayerCharacters(Set<PlayerCharacter> involvedPlayerCharacters) {
        this.involvedPlayerCharacters = involvedPlayerCharacters;
    }

    public Set<Npc> getInvolvedNpcs() {
        return involvedNpcs;
    }

    public void setInvolvedNpcs(Set<Npc> involvedNpcs) {
        this.involvedNpcs = involvedNpcs;
    }
}
