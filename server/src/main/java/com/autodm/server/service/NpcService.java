package com.autodm.server.service;

import com.autodm.server.dto.NpcDto;
import com.autodm.server.model.Campaign;
import com.autodm.server.model.Location;
import com.autodm.server.model.Npc;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.LocationRepository;
import com.autodm.server.repository.NpcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NpcService {

    private final NpcRepository npcRepository;
    private final CampaignRepository campaignRepository;
    private final LocationRepository locationRepository;

    public NpcService(NpcRepository npcRepository, CampaignRepository campaignRepository, LocationRepository locationRepository) {
        this.npcRepository = npcRepository;
        this.campaignRepository = campaignRepository;
        this.locationRepository = locationRepository;
    }

    public List<NpcDto> getNpcsByCampaign(Long campaignId) {
        return npcRepository.findByCampaignId(campaignId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public NpcDto getNpc(Long id) {
        return npcRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("NPC not found"));
    }

    public NpcDto createNpc(NpcDto npcDto) {
        Campaign campaign = campaignRepository.findById(npcDto.getCampaignId())
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        Npc npc = new Npc();
        npc.setCampaign(campaign);
        updateNpcFromDto(npc, npcDto);

        Npc savedNpc = npcRepository.save(npc);
        return mapToDto(savedNpc);
    }

    public NpcDto updateNpc(Long id, NpcDto npcDto) {
        Npc npc = npcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("NPC not found"));

        updateNpcFromDto(npc, npcDto);

        Npc updatedNpc = npcRepository.save(npc);
        return mapToDto(updatedNpc);
    }

    public void deleteNpc(Long id) {
        npcRepository.deleteById(id);
    }

    private void updateNpcFromDto(Npc npc, NpcDto dto) {
        npc.setName(dto.getName());
        npc.setDescription(dto.getDescription());
        npc.setRole(dto.getRole());
        npc.setDisposition(dto.getDisposition());
        npc.setFaction(dto.getFaction());

        if (dto.getCurrentLocationId() != null) {
            Location location = locationRepository.findById(dto.getCurrentLocationId())
                    .orElseThrow(() -> new IllegalArgumentException("Location not found"));
            npc.setCurrentLocation(location);
        } else {
            npc.setCurrentLocation(null);
        }

        npc.setIsAlive(dto.getIsAlive() != null ? dto.getIsAlive() : true);
        npc.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        npc.setRelationshipToParty(dto.getRelationshipToParty());
        npc.setNotes(dto.getNotes());
        npc.setCombatStatistics(dto.getCombatStatistics());
    }

    private NpcDto mapToDto(Npc npc) {
        NpcDto dto = new NpcDto();
        dto.setId(npc.getId());
        dto.setCampaignId(npc.getCampaign().getId());
        dto.setName(npc.getName());
        dto.setDescription(npc.getDescription());
        dto.setRole(npc.getRole());
        dto.setDisposition(npc.getDisposition());
        dto.setFaction(npc.getFaction());
        if (npc.getCurrentLocation() != null) {
            dto.setCurrentLocationId(npc.getCurrentLocation().getId());
        }
        dto.setIsAlive(npc.getIsAlive());
        dto.setIsActive(npc.getIsActive());
        dto.setRelationshipToParty(npc.getRelationshipToParty());
        dto.setNotes(npc.getNotes());
        dto.setCombatStatistics(npc.getCombatStatistics());
        return dto;
    }
}
