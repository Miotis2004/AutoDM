package com.autodm.server.service;

import com.autodm.server.dto.LocationDto;
import com.autodm.server.model.Campaign;
import com.autodm.server.model.Location;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final CampaignRepository campaignRepository;

    public LocationService(LocationRepository locationRepository, CampaignRepository campaignRepository) {
        this.locationRepository = locationRepository;
        this.campaignRepository = campaignRepository;
    }

    @Transactional(readOnly = true)
    public List<LocationDto> getLocationsByCampaign(Long campaignId) {
        return locationRepository.findByCampaignId(campaignId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LocationDto getLocation(Long id) {
        return locationRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
    }

    @Transactional
    public LocationDto createLocation(LocationDto dto) {
        Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found with id: " + dto.getCampaignId()));

        Location location = new Location();
        location.setCampaign(campaign);
        location.setName(dto.getName());
        location.setType(dto.getType());
        location.setDescription(dto.getDescription());
        location.setIsDiscovered(dto.getIsDiscovered() != null ? dto.getIsDiscovered() : false);

        if (dto.getParentLocationId() != null) {
            Location parent = locationRepository.findById(dto.getParentLocationId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent location not found with id: " + dto.getParentLocationId()));
            location.setParentLocation(parent);
        }

        if (dto.getConnectedLocationIds() != null) {
             Set<Location> connected = new HashSet<>();
             for (Long connectedId : dto.getConnectedLocationIds()) {
                 Location connLoc = locationRepository.findById(connectedId)
                         .orElseThrow(() -> new IllegalArgumentException("Connected location not found with id: " + connectedId));
                 connected.add(connLoc);
             }
             location.setConnectedLocations(connected);
        }

        Location saved = locationRepository.save(location);
        return mapToDto(saved);
    }

    @Transactional
    public LocationDto updateLocation(Long id, LocationDto dto) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));

        location.setName(dto.getName());
        location.setType(dto.getType());
        location.setDescription(dto.getDescription());
        location.setIsDiscovered(dto.getIsDiscovered() != null ? dto.getIsDiscovered() : false);

        if (dto.getParentLocationId() != null) {
            Location parent = locationRepository.findById(dto.getParentLocationId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent location not found with id: " + dto.getParentLocationId()));
            location.setParentLocation(parent);
        } else {
            location.setParentLocation(null);
        }

        if (dto.getConnectedLocationIds() != null) {
             Set<Location> connected = new HashSet<>();
             for (Long connectedId : dto.getConnectedLocationIds()) {
                 Location connLoc = locationRepository.findById(connectedId)
                         .orElseThrow(() -> new IllegalArgumentException("Connected location not found with id: " + connectedId));
                 connected.add(connLoc);
             }
             location.setConnectedLocations(connected);
        } else {
            location.setConnectedLocations(new HashSet<>());
        }

        Location saved = locationRepository.save(location);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
        locationRepository.delete(location);
    }

    private LocationDto mapToDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setCampaignId(location.getCampaign().getId());
        dto.setName(location.getName());
        dto.setType(location.getType());
        dto.setDescription(location.getDescription());
        dto.setIsDiscovered(location.getIsDiscovered());

        if (location.getParentLocation() != null) {
            dto.setParentLocationId(location.getParentLocation().getId());
        }

        if (location.getConnectedLocations() != null) {
            Set<Long> connectedIds = location.getConnectedLocations().stream()
                    .map(Location::getId)
                    .collect(Collectors.toSet());
            dto.setConnectedLocationIds(connectedIds);
        }

        return dto;
    }
}
