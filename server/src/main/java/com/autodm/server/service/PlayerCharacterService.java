package com.autodm.server.service;

import com.autodm.server.dto.CharacterResourceDto;
import com.autodm.server.dto.PlayerCharacterDto;
import com.autodm.server.model.Campaign;
import com.autodm.server.model.CharacterResource;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.CharacterResourceRepository;
import com.autodm.server.repository.PlayerCharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerCharacterService {

    private final PlayerCharacterRepository characterRepository;
    private final CharacterResourceRepository resourceRepository;
    private final CampaignRepository campaignRepository;

    public PlayerCharacterService(PlayerCharacterRepository characterRepository,
                                  CharacterResourceRepository resourceRepository,
                                  CampaignRepository campaignRepository) {
        this.characterRepository = characterRepository;
        this.resourceRepository = resourceRepository;
        this.campaignRepository = campaignRepository;
    }

    public List<PlayerCharacterDto> getCharactersByCampaignId(Long campaignId) {
        return characterRepository.findByCampaignId(campaignId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PlayerCharacterDto getCharacterById(Long id) {
        return characterRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    public PlayerCharacterDto createCharacter(PlayerCharacterDto dto) {
        Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));

        PlayerCharacter character = new PlayerCharacter();
        character.setCampaign(campaign);
        updateCharacterFromDto(character, dto);

        return convertToDto(characterRepository.save(character));
    }

    public PlayerCharacterDto updateCharacter(Long id, PlayerCharacterDto dto) {
        PlayerCharacter character = characterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found"));

        updateCharacterFromDto(character, dto);
        return convertToDto(characterRepository.save(character));
    }

    public void deleteCharacter(Long id) {
        characterRepository.deleteById(id);
    }

    public List<CharacterResourceDto> getResourcesByCharacterId(Long characterId) {
        return resourceRepository.findByPlayerCharacterId(characterId).stream()
                .map(this::convertToResourceDto)
                .collect(Collectors.toList());
    }

    public CharacterResourceDto createResource(Long characterId, CharacterResourceDto dto) {
        PlayerCharacter character = characterRepository.findById(characterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found"));

        CharacterResource resource = new CharacterResource();
        resource.setPlayerCharacter(character);
        updateResourceFromDto(resource, dto);

        return convertToResourceDto(resourceRepository.save(resource));
    }

    public CharacterResourceDto updateResource(Long resourceId, CharacterResourceDto dto) {
        CharacterResource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        updateResourceFromDto(resource, dto);
        return convertToResourceDto(resourceRepository.save(resource));
    }

    public void deleteResource(Long resourceId) {
        resourceRepository.deleteById(resourceId);
    }

    private PlayerCharacterDto convertToDto(PlayerCharacter character) {
        PlayerCharacterDto dto = new PlayerCharacterDto();
        dto.setId(character.getId());
        dto.setCampaignId(character.getCampaign().getId());
        dto.setName(character.getName());
        dto.setAncestry(character.getAncestry());
        dto.setCharacterClass(character.getCharacterClass());
        dto.setLevel(character.getLevel());
        dto.setBackground(character.getBackground());
        dto.setAlignment(character.getAlignment());
        dto.setHitPoints(character.getHitPoints());
        dto.setMaximumHitPoints(character.getMaximumHitPoints());
        dto.setTemporaryHitPoints(character.getTemporaryHitPoints());
        dto.setIsUnconscious(character.getIsUnconscious());
        dto.setIsDead(character.getIsDead());
        dto.setArmorClass(character.getArmorClass());
        dto.setMovement(character.getMovement());
        dto.setStrength(character.getStrength());
        dto.setDexterity(character.getDexterity());
        dto.setConstitution(character.getConstitution());
        dto.setIntelligence(character.getIntelligence());
        dto.setWisdom(character.getWisdom());
        dto.setCharisma(character.getCharisma());
        dto.setSavingThrowProficiencies(character.getSavingThrowProficiencies());
        dto.setSkillProficiencies(character.getSkillProficiencies());
        dto.setGeneralProficiencies(character.getGeneralProficiencies());
        dto.setConditions(character.getConditions());
        return dto;
    }

    private void updateCharacterFromDto(PlayerCharacter character, PlayerCharacterDto dto) {
        if (dto.getName() != null) character.setName(dto.getName());
        if (dto.getAncestry() != null) character.setAncestry(dto.getAncestry());
        if (dto.getCharacterClass() != null) character.setCharacterClass(dto.getCharacterClass());
        if (dto.getLevel() != null) character.setLevel(dto.getLevel());
        if (dto.getBackground() != null) character.setBackground(dto.getBackground());
        if (dto.getAlignment() != null) character.setAlignment(dto.getAlignment());
        if (dto.getHitPoints() != null) character.setHitPoints(dto.getHitPoints());
        if (dto.getMaximumHitPoints() != null) character.setMaximumHitPoints(dto.getMaximumHitPoints());
        if (dto.getTemporaryHitPoints() != null) character.setTemporaryHitPoints(dto.getTemporaryHitPoints());
        if (dto.getIsUnconscious() != null) character.setIsUnconscious(dto.getIsUnconscious());
        if (dto.getIsDead() != null) character.setIsDead(dto.getIsDead());
        if (dto.getArmorClass() != null) character.setArmorClass(dto.getArmorClass());
        if (dto.getMovement() != null) character.setMovement(dto.getMovement());
        if (dto.getStrength() != null) character.setStrength(dto.getStrength());
        if (dto.getDexterity() != null) character.setDexterity(dto.getDexterity());
        if (dto.getConstitution() != null) character.setConstitution(dto.getConstitution());
        if (dto.getIntelligence() != null) character.setIntelligence(dto.getIntelligence());
        if (dto.getWisdom() != null) character.setWisdom(dto.getWisdom());
        if (dto.getCharisma() != null) character.setCharisma(dto.getCharisma());
        if (dto.getSavingThrowProficiencies() != null) character.setSavingThrowProficiencies(dto.getSavingThrowProficiencies());
        if (dto.getSkillProficiencies() != null) character.setSkillProficiencies(dto.getSkillProficiencies());
        if (dto.getGeneralProficiencies() != null) character.setGeneralProficiencies(dto.getGeneralProficiencies());
        if (dto.getConditions() != null) character.setConditions(dto.getConditions());
    }

    private CharacterResourceDto convertToResourceDto(CharacterResource resource) {
        CharacterResourceDto dto = new CharacterResourceDto();
        dto.setId(resource.getId());
        dto.setPlayerCharacterId(resource.getPlayerCharacter().getId());
        dto.setName(resource.getName());
        dto.setResourceType(resource.getResourceType());
        dto.setCurrentValue(resource.getCurrentValue());
        dto.setMaximumValue(resource.getMaximumValue());
        return dto;
    }

    private void updateResourceFromDto(CharacterResource resource, CharacterResourceDto dto) {
        if (dto.getName() != null) resource.setName(dto.getName());
        if (dto.getResourceType() != null) resource.setResourceType(dto.getResourceType());
        if (dto.getCurrentValue() != null) resource.setCurrentValue(dto.getCurrentValue());
        if (dto.getMaximumValue() != null) resource.setMaximumValue(dto.getMaximumValue());
    }
}
