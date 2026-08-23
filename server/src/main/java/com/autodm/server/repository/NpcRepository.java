package com.autodm.server.repository;

import com.autodm.server.model.Npc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NpcRepository extends JpaRepository<Npc, Long> {
    List<Npc> findByCampaignId(Long campaignId);
}
