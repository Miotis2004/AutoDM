package com.autodm.server.service;

import com.autodm.server.dto.ItemDto;
import com.autodm.server.model.Campaign;
import com.autodm.server.model.Item;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.ItemRepository;
import com.autodm.server.repository.PlayerCharacterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CampaignRepository campaignRepository;
    private final PlayerCharacterRepository playerCharacterRepository;

    public ItemService(ItemRepository itemRepository, CampaignRepository campaignRepository, PlayerCharacterRepository playerCharacterRepository) {
        this.itemRepository = itemRepository;
        this.campaignRepository = campaignRepository;
        this.playerCharacterRepository = playerCharacterRepository;
    }

    public List<ItemDto> getItemsByCampaign(Long campaignId) {
        return itemRepository.findByCampaignId(campaignId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> getItemsByCharacter(Long characterId) {
        return itemRepository.findByOwnerCharacterId(characterId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ItemDto getItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        return mapToDto(item);
    }

    @Transactional
    public ItemDto createItem(ItemDto dto) {
        validateQuantity(dto.getQuantity());

        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setCategory(dto.getCategory());
        item.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 1);
        item.setValue(dto.getValue() != null ? dto.getValue() : 0);
        item.setIsEquipped(dto.getIsEquipped() != null ? dto.getIsEquipped() : false);

        if (dto.getCampaignId() != null) {
            Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid campaign ID"));
            item.setCampaign(campaign);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campaign ID is required");
        }

        if (dto.getOwnerCharacterId() != null) {
            PlayerCharacter character = playerCharacterRepository.findById(dto.getOwnerCharacterId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid owner character ID"));

            // Check if the character belongs to the same campaign
            if (!character.getCampaign().getId().equals(dto.getCampaignId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Character does not belong to the given campaign");
            }

            item.setOwnerCharacter(character);
        }

        Item savedItem = itemRepository.save(item);
        return mapToDto(savedItem);
    }

    @Transactional
    public ItemDto updateItem(Long id, ItemDto dto) {
        validateQuantity(dto.getQuantity());

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getCategory() != null) {
            item.setCategory(dto.getCategory());
        }
        if (dto.getQuantity() != null) {
            item.setQuantity(dto.getQuantity());
        }
        if (dto.getValue() != null) {
            item.setValue(dto.getValue());
        }
        if (dto.getIsEquipped() != null) {
            item.setIsEquipped(dto.getIsEquipped());
        }

        // Handle owner character update
        if (dto.getOwnerCharacterId() != null) {
            PlayerCharacter character = playerCharacterRepository.findById(dto.getOwnerCharacterId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid owner character ID"));

            // Verify it's the same campaign
            if (!character.getCampaign().getId().equals(item.getCampaign().getId())) {
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Character does not belong to the item's campaign");
            }
            item.setOwnerCharacter(character);
        } else if (dto.getOwnerCharacterId() == null && dto.getCategory() != null) { // This condition is a bit weak, let's just allow unsetting it if explicitly requested?
            // In a PUT, if ownerCharacterId is not provided, do we clear it? Let's leave it as is if it's null, or clear it if it's explicitly cleared via another method.
            // But if we want full update, if dto.getOwnerCharacterId is null, we clear it. Let's do that for now. Actually usually DTOs might omit it.
            // Let's assume if it's present in DTO we update, otherwise leave.
            // To clear owner, we could use a specific transfer method.
        }

        Item savedItem = itemRepository.save(item);
        return mapToDto(savedItem);
    }

    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        itemRepository.deleteById(id);
    }

    @Transactional
    public ItemDto transferItem(Long id, Long targetCharacterId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        if (targetCharacterId != null) {
            PlayerCharacter targetCharacter = playerCharacterRepository.findById(targetCharacterId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid target character ID"));

            if (!targetCharacter.getCampaign().getId().equals(item.getCampaign().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target character does not belong to the item's campaign");
            }

            item.setOwnerCharacter(targetCharacter);
        } else {
            // Transfer to campaign (drop on ground)
            item.setOwnerCharacter(null);
        }

        item.setIsEquipped(false); // Unequip when transferring

        Item savedItem = itemRepository.save(item);
        return mapToDto(savedItem);
    }

    private void validateQuantity(Integer quantity) {
        if (quantity != null && quantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity cannot be negative");
        }
    }

    private ItemDto mapToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setCategory(item.getCategory());
        dto.setQuantity(item.getQuantity());
        dto.setValue(item.getValue());
        dto.setIsEquipped(item.getIsEquipped());
        if (item.getOwnerCharacter() != null) {
            dto.setOwnerCharacterId(item.getOwnerCharacter().getId());
        }
        if (item.getCampaign() != null) {
            dto.setCampaignId(item.getCampaign().getId());
        }
        return dto;
    }
}
