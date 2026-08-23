package com.autodm.server.service;

import com.autodm.server.dto.FactionDto;
import com.autodm.server.model.Campaign;
import com.autodm.server.model.Faction;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.FactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FactionService {

    private final FactionRepository factionRepository;
    private final CampaignRepository campaignRepository;

    public FactionService(FactionRepository factionRepository, CampaignRepository campaignRepository) {
        this.factionRepository = factionRepository;
        this.campaignRepository = campaignRepository;
    }

    public List<FactionDto> getFactionsByCampaign(Long campaignId) {
        return factionRepository.findByCampaignId(campaignId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public FactionDto getFaction(Long id) {
        return factionRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Faction not found"));
    }

    public FactionDto createFaction(FactionDto factionDto) {
        Campaign campaign = campaignRepository.findById(factionDto.getCampaignId())
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        Faction faction = new Faction();
        faction.setCampaign(campaign);
        updateFactionFromDto(faction, factionDto);

        Faction savedFaction = factionRepository.save(faction);
        return mapToDto(savedFaction);
    }

    public FactionDto updateFaction(Long id, FactionDto factionDto) {
        Faction faction = factionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Faction not found"));

        updateFactionFromDto(faction, factionDto);

        Faction updatedFaction = factionRepository.save(faction);
        return mapToDto(updatedFaction);
    }

    public void deleteFaction(Long id) {
        factionRepository.deleteById(id);
    }

    private void updateFactionFromDto(Faction faction, FactionDto dto) {
        faction.setName(dto.getName());
        faction.setDescription(dto.getDescription());
        faction.setDisposition(dto.getDisposition());
        faction.setReputation(dto.getReputation());
        faction.setNotes(dto.getNotes());

        faction.getAllies().clear();
        if (dto.getAllyIds() != null) {
            for (Long allyId : dto.getAllyIds()) {
                Faction ally = factionRepository.findById(allyId)
                        .orElseThrow(() -> new IllegalArgumentException("Ally faction not found"));
                faction.getAllies().add(ally);
            }
        }

        faction.getEnemies().clear();
        if (dto.getEnemyIds() != null) {
            for (Long enemyId : dto.getEnemyIds()) {
                Faction enemy = factionRepository.findById(enemyId)
                        .orElseThrow(() -> new IllegalArgumentException("Enemy faction not found"));
                faction.getEnemies().add(enemy);
            }
        }
    }

    private FactionDto mapToDto(Faction faction) {
        FactionDto dto = new FactionDto();
        dto.setId(faction.getId());
        dto.setCampaignId(faction.getCampaign().getId());
        dto.setName(faction.getName());
        dto.setDescription(faction.getDescription());
        dto.setDisposition(faction.getDisposition());
        dto.setReputation(faction.getReputation());
        dto.setNotes(faction.getNotes());

        if (faction.getAllies() != null) {
            dto.setAllyIds(faction.getAllies().stream().map(Faction::getId).collect(Collectors.toSet()));
        }

        if (faction.getEnemies() != null) {
            dto.setEnemyIds(faction.getEnemies().stream().map(Faction::getId).collect(Collectors.toSet()));
        }

        return dto;
    }
}
