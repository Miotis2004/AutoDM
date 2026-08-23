package com.autodm.server.repository;

import com.autodm.server.model.Scene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SceneRepository extends JpaRepository<Scene, Long> {
    List<Scene> findByCampaignId(Long campaignId);
}
