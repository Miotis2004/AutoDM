package com.autodm.server.service;

import com.autodm.server.model.Combatant;
import com.autodm.server.model.CreatureTemplate;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CombatantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnemyBehaviorService {

    private final CombatantRepository combatantRepository;
    private final CombatResolutionService combatResolutionService;

    public EnemyBehaviorService(CombatantRepository combatantRepository, CombatResolutionService combatResolutionService) {
        this.combatantRepository = combatantRepository;
        this.combatResolutionService = combatResolutionService;
    }

    /**
     * Executes an enemy's turn. Selects a valid target, attacks, applies damage.
     * @return a message describing the action taken, or null if no action possible
     */
    @Transactional
    public String executeEnemyTurn(Long encounterId, Long combatantId) {
        Combatant enemy = combatantRepository.findById(combatantId)
                .orElseThrow(() -> new IllegalArgumentException("Combatant not found"));

        if (enemy.getIsPlayer() || enemy.getIsDefeated()) {
            return enemy.getName() + " cannot take a turn (is player or defeated).";
        }

        // 1. Select a valid living target
        List<Combatant> possibleTargets = combatantRepository.findByEncounterIdOrderByInitiativeDesc(encounterId)
                .stream()
                .filter(Combatant::getIsPlayer)
                .filter(c -> !c.getIsDefeated())
                .collect(Collectors.toList());

        if (possibleTargets.isEmpty()) {
            return enemy.getName() + " finds no valid targets.";
        }

        // Deterministic target selection: Lowest HP first, then highest initiative (which is order in the list)
        possibleTargets.sort((c1, c2) -> {
            int hpCompare = c1.getHitPoints().compareTo(c2.getHitPoints());
            if (hpCompare != 0) {
                return hpCompare;
            }
            return c2.getInitiative().compareTo(c1.getInitiative());
        });
        Combatant target = possibleTargets.get(0);

        // 2. Perform attack
        CreatureTemplate template = enemy.getTemplate();
        int attackBonus = 0;
        String damageNotation = "1d4"; // Default fallback
        if (template != null) {
            attackBonus = template.getAttackBonus() != null ? template.getAttackBonus() : 0;
            damageNotation = template.getDamage() != null ? template.getDamage() : "1d4";
        }

        AttackResult result = combatResolutionService.resolveAttack(enemy, target, attackBonus, damageNotation);
        return result.getDescription();
    }
}
