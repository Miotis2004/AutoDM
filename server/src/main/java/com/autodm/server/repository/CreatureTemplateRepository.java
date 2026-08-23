package com.autodm.server.repository;

import com.autodm.server.model.CreatureTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreatureTemplateRepository extends JpaRepository<CreatureTemplate, Long> {
    List<CreatureTemplate> findByCampaignId(Long campaignId);
}
