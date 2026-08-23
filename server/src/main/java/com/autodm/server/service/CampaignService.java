package com.autodm.server.service;

import com.autodm.server.dto.CampaignDto;
import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignStatus;
import com.autodm.server.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Transactional(readOnly = true)
    public List<CampaignDto> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CampaignDto getCampaign(Long id) {
        return campaignRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found with id: " + id));
    }

    @Transactional
    public CampaignDto createCampaign(CampaignDto dto) {
        Campaign campaign = new Campaign();
        campaign.setTitle(dto.getTitle());
        campaign.setDescription(dto.getDescription());
        campaign.setNotes(dto.getNotes());
        // creationDate and status are set by the constructor of Campaign

        Campaign saved = campaignRepository.save(campaign);
        return mapToDto(saved);
    }

    @Transactional
    public CampaignDto updateCampaign(Long id, CampaignDto dto) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found with id: " + id));

        if (dto.getTitle() != null) {
            campaign.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            campaign.setDescription(dto.getDescription());
        }
        if (dto.getNotes() != null) {
            campaign.setNotes(dto.getNotes());
        }
        if (dto.getStatus() != null) {
            campaign.setStatus(dto.getStatus());
        }

        // Locations and scenes would typically be set through their respective services or specific actions,
        // but we might want to update them directly if we assume they are provided in DTO.
        // For now, let's keep it simple and omit mapping of Location/Scene here unless required.

        Campaign saved = campaignRepository.save(campaign);
        return mapToDto(saved);
    }

    @Transactional
    public CampaignDto archiveCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found with id: " + id));

        campaign.setStatus(CampaignStatus.ARCHIVED);
        Campaign saved = campaignRepository.save(campaign);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found with id: " + id));
        campaignRepository.delete(campaign);
    }

    private CampaignDto mapToDto(Campaign campaign) {
        CampaignDto dto = new CampaignDto();
        dto.setId(campaign.getId());
        dto.setTitle(campaign.getTitle());
        dto.setDescription(campaign.getDescription());
        dto.setStatus(campaign.getStatus());
        dto.setCreationDate(campaign.getCreationDate());
        dto.setLastPlayedDate(campaign.getLastPlayedDate());
        dto.setNotes(campaign.getNotes());

        if (campaign.getCurrentLocation() != null) {
            dto.setCurrentLocationId(campaign.getCurrentLocation().getId());
        }
        if (campaign.getCurrentScene() != null) {
            dto.setCurrentSceneId(campaign.getCurrentScene().getId());
        }

        return dto;
    }
}
