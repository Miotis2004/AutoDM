package com.autodm.server.controller;

import com.autodm.server.dto.CampaignDto;
import com.autodm.server.service.CampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<List<CampaignDto>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDto> getCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaign(id));
    }

    @PostMapping
    public ResponseEntity<CampaignDto> createCampaign(@Valid @RequestBody CampaignDto campaignDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignService.createCampaign(campaignDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignDto> updateCampaign(@PathVariable Long id, @Valid @RequestBody CampaignDto campaignDto) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, campaignDto));
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<CampaignDto> archiveCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.archiveCampaign(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
