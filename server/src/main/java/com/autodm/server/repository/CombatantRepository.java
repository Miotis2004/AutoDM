package com.autodm.server.repository;

import com.autodm.server.model.Combatant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CombatantRepository extends JpaRepository<Combatant, Long> {
    List<Combatant> findByEncounterIdOrderByInitiativeDesc(Long encounterId);
    List<Combatant> findByPlayerCharacterId(Long playerCharacterId);
}
