package com.autodm.server.repository;

import com.autodm.server.model.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    List<GameSession> findByCampaignIdOrderByStartTimeDesc(Long campaignId);
}
