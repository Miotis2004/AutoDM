package com.autodm.server.service;

import com.autodm.server.dto.ObjectiveDto;
import com.autodm.server.dto.QuestDto;
import com.autodm.server.model.*;
import com.autodm.server.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestService {

    private final QuestRepository questRepository;
    private final ObjectiveRepository objectiveRepository;
    private final CampaignRepository campaignRepository;
    private final NpcRepository npcRepository;
    private final LocationRepository locationRepository;

    public QuestService(QuestRepository questRepository, ObjectiveRepository objectiveRepository,
                        CampaignRepository campaignRepository, NpcRepository npcRepository,
                        LocationRepository locationRepository) {
        this.questRepository = questRepository;
        this.objectiveRepository = objectiveRepository;
        this.campaignRepository = campaignRepository;
        this.npcRepository = npcRepository;
        this.locationRepository = locationRepository;
    }

    public List<QuestDto> getQuestsByCampaign(Long campaignId) {
        return questRepository.findByCampaignId(campaignId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public QuestDto getQuest(Long id) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest not found"));
        return mapToDto(quest);
    }

    public QuestDto createQuest(QuestDto dto) {
        Campaign campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));

        Quest quest = new Quest();
        quest.setCampaign(campaign);
        quest.setTitle(dto.getTitle());
        quest.setDescription(dto.getDescription());
        quest.setStatus(dto.getStatus() != null ? dto.getStatus() : QuestStatus.ACTIVE);
        quest.setRewards(dto.getRewards());
        quest.setNotes(dto.getNotes());

        if (dto.getQuestGiverId() != null) {
            Npc giver = npcRepository.findById(dto.getQuestGiverId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest giver not found"));
            if (!giver.getCampaign().getId().equals(dto.getCampaignId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quest giver does not belong to the same campaign");
            }
            quest.setQuestGiver(giver);
        }

        if (dto.getRelatedLocationIds() != null && !dto.getRelatedLocationIds().isEmpty()) {
            Set<Location> locations = dto.getRelatedLocationIds().stream()
                    .map(locId -> {
                        Location loc = locationRepository.findById(locId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: " + locId));
                        if (!loc.getCampaign().getId().equals(dto.getCampaignId())) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location does not belong to the same campaign");
                        }
                        return loc;
                    })
                    .collect(Collectors.toSet());
            quest.setRelatedLocations(locations);
        }

        if (dto.getObjectives() != null) {
            for (ObjectiveDto objDto : dto.getObjectives()) {
                Objective objective = new Objective();
                objective.setDescription(objDto.getDescription());
                objective.setIsCompleted(objDto.getIsCompleted() != null ? objDto.getIsCompleted() : false);
                quest.addObjective(objective);
            }
        }

        quest = questRepository.save(quest);
        return mapToDto(quest);
    }

    public QuestDto updateQuest(Long id, QuestDto dto) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest not found"));

        quest.setTitle(dto.getTitle());
        quest.setDescription(dto.getDescription());
        if (dto.getStatus() != null) {
            quest.setStatus(dto.getStatus());
        }
        quest.setRewards(dto.getRewards());
        quest.setNotes(dto.getNotes());

        if (dto.getQuestGiverId() != null) {
            Npc giver = npcRepository.findById(dto.getQuestGiverId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest giver not found"));
            if (!giver.getCampaign().getId().equals(quest.getCampaign().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quest giver does not belong to the same campaign");
            }
            quest.setQuestGiver(giver);
        } else {
            quest.setQuestGiver(null);
        }

        if (dto.getRelatedLocationIds() != null) {
            final Long questCampaignId = quest.getCampaign().getId();
            Set<Location> locations = dto.getRelatedLocationIds().stream()
                    .map(locId -> {
                        Location loc = locationRepository.findById(locId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: " + locId));
                        if (!loc.getCampaign().getId().equals(questCampaignId)) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location does not belong to the same campaign");
                        }
                        return loc;
                    })
                    .collect(Collectors.toSet());
            quest.setRelatedLocations(locations);
        } else {
            quest.getRelatedLocations().clear();
        }

        // Handle objectives
        if (dto.getObjectives() != null) {
            // Remove missing
            List<Objective> toRemove = quest.getObjectives().stream()
                    .filter(o -> o.getId() != null && dto.getObjectives().stream().noneMatch(doDto -> o.getId().equals(doDto.getId())))
                    .collect(Collectors.toList());
            toRemove.forEach(quest::removeObjective);

            // Add or update
            for (ObjectiveDto objDto : dto.getObjectives()) {
                if (objDto.getId() != null) {
                    Objective existing = quest.getObjectives().stream()
                            .filter(o -> objDto.getId().equals(o.getId()))
                            .findFirst()
                            .orElse(null);
                    if (existing != null) {
                        existing.setDescription(objDto.getDescription());
                        existing.setIsCompleted(objDto.getIsCompleted() != null ? objDto.getIsCompleted() : existing.getIsCompleted());
                    }
                } else {
                    Objective newObj = new Objective();
                    newObj.setDescription(objDto.getDescription());
                    newObj.setIsCompleted(objDto.getIsCompleted() != null ? objDto.getIsCompleted() : false);
                    quest.addObjective(newObj);
                }
            }
        } else {
            quest.getObjectives().clear();
        }

        quest = questRepository.save(quest);
        return mapToDto(quest);
    }

    public void deleteQuest(Long id) {
        if (!questRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest not found");
        }
        questRepository.deleteById(id);
    }

    public ObjectiveDto completeObjective(Long objectiveId) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found"));
        objective.setIsCompleted(true);
        objective = objectiveRepository.save(objective);
        return mapObjectiveToDto(objective);
    }

    public QuestDto updateQuestStatus(Long questId, QuestStatus status) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest not found"));
        quest.setStatus(status);
        quest = questRepository.save(quest);
        return mapToDto(quest);
    }

    private QuestDto mapToDto(Quest quest) {
        QuestDto dto = new QuestDto();
        dto.setId(quest.getId());
        dto.setCampaignId(quest.getCampaign().getId());
        dto.setTitle(quest.getTitle());
        dto.setDescription(quest.getDescription());
        dto.setStatus(quest.getStatus());
        dto.setQuestGiverId(quest.getQuestGiver() != null ? quest.getQuestGiver().getId() : null);
        dto.setRewards(quest.getRewards());
        dto.setNotes(quest.getNotes());

        Set<Long> locationIds = quest.getRelatedLocations().stream()
                .map(Location::getId)
                .collect(Collectors.toSet());
        dto.setRelatedLocationIds(locationIds);

        List<ObjectiveDto> objectiveDtos = quest.getObjectives().stream()
                .map(this::mapObjectiveToDto)
                .collect(Collectors.toList());
        dto.setObjectives(objectiveDtos);

        return dto;
    }

    private ObjectiveDto mapObjectiveToDto(Objective objective) {
        ObjectiveDto dto = new ObjectiveDto();
        dto.setId(objective.getId());
        dto.setQuestId(objective.getQuest().getId());
        dto.setDescription(objective.getDescription());
        dto.setIsCompleted(objective.getIsCompleted());
        return dto;
    }
}
