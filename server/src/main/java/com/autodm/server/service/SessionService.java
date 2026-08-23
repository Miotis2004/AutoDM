package com.autodm.server.service;

import com.autodm.server.dto.CampaignEventDto;
import com.autodm.server.dto.GameSessionDto;
import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.GameSession;
import com.autodm.server.repository.CampaignEventRepository;
import com.autodm.server.repository.CampaignRepository;
import com.autodm.server.repository.GameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final GameSessionRepository gameSessionRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignEventRepository campaignEventRepository;
    private final EventService eventService;

    public SessionService(GameSessionRepository gameSessionRepository,
                          CampaignRepository campaignRepository,
                          CampaignEventRepository campaignEventRepository,
                          EventService eventService) {
        this.gameSessionRepository = gameSessionRepository;
        this.campaignRepository = campaignRepository;
        this.campaignEventRepository = campaignEventRepository;
        this.eventService = eventService;
    }

    @Transactional
    public GameSessionDto startSession(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));

        // Update campaign's last played date
        campaign.setLastPlayedDate(LocalDateTime.now());
        campaignRepository.save(campaign);

        // Close any active sessions for this campaign
        List<GameSession> existingSessions = gameSessionRepository.findByCampaignIdOrderByStartTimeDesc(campaignId);
        for (GameSession s : existingSessions) {
            if (s.getEndTime() == null) {
                s.setEndTime(LocalDateTime.now());
                gameSessionRepository.save(s);
            }
        }

        GameSession session = new GameSession(campaign, LocalDateTime.now());
        session = gameSessionRepository.save(session);

        eventService.logSessionStart(campaign, "Started a new session.");

        return mapToDto(session);
    }

    @Transactional
    public GameSessionDto resumeSession(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));

        List<GameSession> sessions = gameSessionRepository.findByCampaignIdOrderByStartTimeDesc(campaignId);
        GameSession activeSession = null;
        for (GameSession s : sessions) {
            if (s.getEndTime() == null) {
                activeSession = s;
                break;
            }
        }

        if (activeSession != null) {
            // Already have an active session, just resume it
            campaign.setLastPlayedDate(LocalDateTime.now());
            campaignRepository.save(campaign);
            eventService.logSessionStart(campaign, "Resumed existing session.");
            return mapToDto(activeSession);
        } else {
            // Start a new one if none active
            return startSession(campaignId);
        }
    }

    @Transactional
    public GameSessionDto endSession(Long sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        if (session.getEndTime() == null) {
            session.setEndTime(LocalDateTime.now());
            session = gameSessionRepository.save(session);
            eventService.logSessionEnd(session.getCampaign(), "Ended the session.");
        }

        return mapToDto(session);
    }

    @Transactional(readOnly = true)
    public List<GameSessionDto> getCampaignSessions(Long campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found");
        }
        return gameSessionRepository.findByCampaignIdOrderByStartTimeDesc(campaignId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CampaignEventDto> getCampaignEvents(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));
        return campaignEventRepository.findByCampaignOrderByTimestampDesc(campaign)
                .stream()
                .map(this::mapToEventDto)
                .collect(Collectors.toList());
    }

    private GameSessionDto mapToDto(GameSession session) {
        GameSessionDto dto = new GameSessionDto();
        dto.setId(session.getId());
        dto.setCampaignId(session.getCampaign().getId());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        return dto;
    }

    private CampaignEventDto mapToEventDto(CampaignEvent event) {
        CampaignEventDto dto = new CampaignEventDto();
        dto.setId(event.getId());
        dto.setCampaignId(event.getCampaign().getId());
        dto.setEventType(event.getEventType());
        dto.setTimestamp(event.getTimestamp());
        dto.setDescription(event.getDescription());
        return dto;
    }
}
