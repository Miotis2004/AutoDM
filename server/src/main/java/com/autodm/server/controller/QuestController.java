package com.autodm.server.controller;

import com.autodm.server.dto.ObjectiveDto;
import com.autodm.server.dto.QuestDto;
import com.autodm.server.model.QuestStatus;
import com.autodm.server.service.QuestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuestController {

    private final QuestService questService;

    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping("/campaigns/{campaignId}/quests")
    public ResponseEntity<List<QuestDto>> getQuestsByCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(questService.getQuestsByCampaign(campaignId));
    }

    @GetMapping("/quests/{id}")
    public ResponseEntity<QuestDto> getQuest(@PathVariable Long id) {
        return ResponseEntity.ok(questService.getQuest(id));
    }

    @PostMapping("/campaigns/{campaignId}/quests")
    public ResponseEntity<QuestDto> createQuest(@PathVariable Long campaignId, @Valid @RequestBody QuestDto questDto) {
        questDto.setCampaignId(campaignId);
        return ResponseEntity.status(HttpStatus.CREATED).body(questService.createQuest(questDto));
    }

    @PutMapping("/quests/{id}")
    public ResponseEntity<QuestDto> updateQuest(@PathVariable Long id, @Valid @RequestBody QuestDto questDto) {
        return ResponseEntity.ok(questService.updateQuest(id, questDto));
    }

    @DeleteMapping("/quests/{id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable Long id) {
        questService.deleteQuest(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/quests/{id}/status")
    public ResponseEntity<QuestDto> updateQuestStatus(@PathVariable Long id, @RequestBody QuestStatusRequest request) {
        return ResponseEntity.ok(questService.updateQuestStatus(id, request.getStatus()));
    }

    @PutMapping("/objectives/{id}/complete")
    public ResponseEntity<ObjectiveDto> completeObjective(@PathVariable Long id) {
        return ResponseEntity.ok(questService.completeObjective(id));
    }

    public static class QuestStatusRequest {
        private QuestStatus status;

        public QuestStatus getStatus() {
            return status;
        }

        public void setStatus(QuestStatus status) {
            this.status = status;
        }
    }
}
