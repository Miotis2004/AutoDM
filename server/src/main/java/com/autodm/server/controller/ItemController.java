package com.autodm.server.controller;

import com.autodm.server.dto.ItemDto;
import com.autodm.server.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<ItemDto>> getItemsByCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(itemService.getItemsByCampaign(campaignId));
    }

    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<ItemDto>> getItemsByCharacter(@PathVariable Long characterId) {
        return ResponseEntity.ok(itemService.getItemsByCharacter(characterId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto dto) {
        return ResponseEntity.ok(itemService.updateItem(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/transfer")
    public ResponseEntity<ItemDto> transferItem(
            @PathVariable Long id,
            @RequestBody Map<String, Long> payload) {

        Long targetCharacterId = payload.get("targetCharacterId");
        return ResponseEntity.ok(itemService.transferItem(id, targetCharacterId));
    }
}
