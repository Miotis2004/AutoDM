package com.autodm.server.controller;

import com.autodm.server.dto.FactionDto;
import com.autodm.server.service.FactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factions")
public class FactionController {

    private final FactionService factionService;

    public FactionController(FactionService factionService) {
        this.factionService = factionService;
    }

    @GetMapping
    public ResponseEntity<List<FactionDto>> getFactionsByCampaign(@RequestParam Long campaignId) {
        return ResponseEntity.ok(factionService.getFactionsByCampaign(campaignId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactionDto> getFaction(@PathVariable Long id) {
        return ResponseEntity.ok(factionService.getFaction(id));
    }

    @PostMapping
    public ResponseEntity<FactionDto> createFaction(@RequestBody FactionDto factionDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(factionService.createFaction(factionDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FactionDto> updateFaction(@PathVariable Long id, @RequestBody FactionDto factionDto) {
        return ResponseEntity.ok(factionService.updateFaction(id, factionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaction(@PathVariable Long id) {
        factionService.deleteFaction(id);
        return ResponseEntity.noContent().build();
    }
}
