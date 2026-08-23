package com.autodm.server.controller;

import com.autodm.server.dto.NpcDto;
import com.autodm.server.service.NpcService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/npcs")
public class NpcController {

    private final NpcService npcService;

    public NpcController(NpcService npcService) {
        this.npcService = npcService;
    }

    @GetMapping
    public ResponseEntity<List<NpcDto>> getNpcsByCampaign(@RequestParam Long campaignId) {
        return ResponseEntity.ok(npcService.getNpcsByCampaign(campaignId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NpcDto> getNpc(@PathVariable Long id) {
        return ResponseEntity.ok(npcService.getNpc(id));
    }

    @PostMapping
    public ResponseEntity<NpcDto> createNpc(@Valid @RequestBody NpcDto npcDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(npcService.createNpc(npcDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NpcDto> updateNpc(@PathVariable Long id, @Valid @RequestBody NpcDto npcDto) {
        return ResponseEntity.ok(npcService.updateNpc(id, npcDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNpc(@PathVariable Long id) {
        npcService.deleteNpc(id);
        return ResponseEntity.noContent().build();
    }
}
