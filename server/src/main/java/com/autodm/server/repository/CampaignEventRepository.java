package com.autodm.server.repository;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.CampaignEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignEventRepository extends JpaRepository<CampaignEvent, Long> {
    List<CampaignEvent> findByCampaignOrderByTimestampDesc(Campaign campaign);
}
