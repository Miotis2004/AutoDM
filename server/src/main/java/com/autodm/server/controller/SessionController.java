package com.autodm.server.controller;

import com.autodm.server.dto.CampaignEventDto;
import com.autodm.server.dto.GameSessionDto;
import com.autodm.server.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/campaigns/{id}/sessions/start")
    public ResponseEntity<GameSessionDto> startSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.startSession(id));
    }

    @PostMapping("/campaigns/{id}/sessions/resume")
    public ResponseEntity<GameSessionDto> resumeSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.resumeSession(id));
    }

    @PostMapping("/sessions/{id}/end")
    public ResponseEntity<GameSessionDto> endSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.endSession(id));
    }

    @GetMapping("/campaigns/{id}/sessions")
    public ResponseEntity<List<GameSessionDto>> getCampaignSessions(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getCampaignSessions(id));
    }

    @GetMapping("/campaigns/{id}/events")
    public ResponseEntity<List<CampaignEventDto>> getCampaignEvents(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getCampaignEvents(id));
    }
}
