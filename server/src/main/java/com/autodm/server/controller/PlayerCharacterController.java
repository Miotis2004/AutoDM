package com.autodm.server.controller;

import com.autodm.server.dto.CharacterResourceDto;
import com.autodm.server.dto.PlayerCharacterDto;
import com.autodm.server.service.PlayerCharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
@CrossOrigin
public class PlayerCharacterController {

    private final PlayerCharacterService characterService;

    public PlayerCharacterController(PlayerCharacterService characterService) {
        this.characterService = characterService;
    }

    // Characters

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<PlayerCharacterDto>> getCharactersByCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(characterService.getCharactersByCampaignId(campaignId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerCharacterDto> getCharacter(@PathVariable Long id) {
        PlayerCharacterDto character = characterService.getCharacterById(id);
        if (character == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(character);
    }

    @PostMapping
    public ResponseEntity<PlayerCharacterDto> createCharacter(@RequestBody PlayerCharacterDto dto) {
        return ResponseEntity.ok(characterService.createCharacter(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerCharacterDto> updateCharacter(@PathVariable Long id, @RequestBody PlayerCharacterDto dto) {
        try {
            return ResponseEntity.ok(characterService.updateCharacter(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }

    // Resources

    @GetMapping("/{characterId}/resources")
    public ResponseEntity<List<CharacterResourceDto>> getResourcesByCharacter(@PathVariable Long characterId) {
        return ResponseEntity.ok(characterService.getResourcesByCharacterId(characterId));
    }

    @PostMapping("/{characterId}/resources")
    public ResponseEntity<CharacterResourceDto> createResource(@PathVariable Long characterId, @RequestBody CharacterResourceDto dto) {
        try {
            return ResponseEntity.ok(characterService.createResource(characterId, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/resources/{resourceId}")
    public ResponseEntity<CharacterResourceDto> updateResource(@PathVariable Long resourceId, @RequestBody CharacterResourceDto dto) {
        try {
            return ResponseEntity.ok(characterService.updateResource(resourceId, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long resourceId) {
        characterService.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }
}
