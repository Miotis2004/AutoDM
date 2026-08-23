package com.autodm.server.repository;

import com.autodm.server.model.Faction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactionRepository extends JpaRepository<Faction, Long> {
    List<Faction> findByCampaignId(Long campaignId);
}
