package com.autodm.server.controller;

import com.autodm.server.dto.LocationDto;
import com.autodm.server.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getLocationsByCampaign(@RequestParam Long campaignId) {
        return ResponseEntity.ok(locationService.getLocationsByCampaign(campaignId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocation(id));
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody LocationDto locationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(locationDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long id, @RequestBody LocationDto locationDto) {
        return ResponseEntity.ok(locationService.updateLocation(id, locationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
