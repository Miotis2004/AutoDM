package com.autodm.server.service;

import com.autodm.server.model.Combatant;
import com.autodm.server.model.Encounter;
import com.autodm.server.model.EncounterStatus;
import com.autodm.server.repository.CombatantRepository;
import com.autodm.server.repository.EncounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EncounterEngineService {

    private final EncounterRepository encounterRepository;
    private final CombatantRepository combatantRepository;
    private final DiceService diceService;
    private final EnemyBehaviorService enemyBehaviorService;

    public EncounterEngineService(EncounterRepository encounterRepository,
                                  CombatantRepository combatantRepository,
                                  DiceService diceService,
                                  EnemyBehaviorService enemyBehaviorService) {
        this.encounterRepository = encounterRepository;
        this.combatantRepository = combatantRepository;
        this.diceService = diceService;
        this.enemyBehaviorService = enemyBehaviorService;
    }

    /**
     * Starts an encounter by rolling initiative for combatants that don't have it,
     * sorting them, and setting the encounter status to ACTIVE.
     */
    @Transactional
    public Encounter startEncounter(Long encounterId) {
        Encounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Encounter not found"));

        if (encounter.getStatus() != EncounterStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Encounter is already started or completed.");
        }

        List<Combatant> combatants = combatantRepository.findByEncounterIdOrderByInitiativeDesc(encounterId);

        for (Combatant combatant : combatants) {
            if (combatant.getInitiative() == null) {
                // Assuming d20 + (dex mod) in the future. For now, flat d20 roll if missing.
                int initRoll = diceService.roll(1, 20, 0).getTotal(); // 1d20
                combatant.setInitiative(initRoll);
                combatantRepository.save(combatant);
            }
        }

        // Re-fetch or re-sort combatants to get the proper turn order
        combatants = combatantRepository.findByEncounterIdOrderByInitiativeDesc(encounterId);

        encounter.setStatus(EncounterStatus.ACTIVE);
        encounter.setCurrentTurn(1); // Round 1

        if (!combatants.isEmpty()) {
            // Find first undefeated combatant
            Optional<Combatant> firstCombatant = combatants.stream()
                .filter(c -> !c.getIsDefeated())
                .findFirst();

            firstCombatant.ifPresent(c -> encounter.setActiveCombatantId(c.getId()));
        }

        return encounterRepository.save(encounter);
    }

    /**
     * Advances to the next turn in the encounter.
     * Skips defeated combatants.
     * If all players or all enemies are defeated, completes the encounter.
     */
    @Transactional
    public Encounter advanceTurn(Long encounterId) {
        Encounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Encounter not found"));

        if (encounter.getStatus() != EncounterStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot advance turn. Encounter is not active.");
        }

        if (checkEncounterCompletion(encounterId)) {
            encounter.setStatus(EncounterStatus.COMPLETED);
            return encounterRepository.save(encounter);
        }

        List<Combatant> combatants = combatantRepository.findByEncounterIdOrderByInitiativeDesc(encounterId);

        if (combatants.isEmpty() || combatants.stream().allMatch(Combatant::getIsDefeated)) {
            encounter.setStatus(EncounterStatus.COMPLETED);
            return encounterRepository.save(encounter);
        }

        Long currentCombatantId = encounter.getActiveCombatantId();
        int currentIndex = -1;
        for (int i = 0; i < combatants.size(); i++) {
            if (combatants.get(i).getId().equals(currentCombatantId)) {
                currentIndex = i;
                break;
            }
        }

        int nextIndex = currentIndex;
        boolean roundWrapped = false;

        do {
            nextIndex++;
            if (nextIndex >= combatants.size()) {
                nextIndex = 0;
                roundWrapped = true;
            }
        } while (combatants.get(nextIndex).getIsDefeated());

        if (roundWrapped || currentIndex == -1) {
            encounter.setCurrentTurn(encounter.getCurrentTurn() + 1);
        }

        encounter.setActiveCombatantId(combatants.get(nextIndex).getId());

        // Re-check completion just in case
        if (checkEncounterCompletion(encounterId)) {
            encounter.setStatus(EncounterStatus.COMPLETED);
        }

        return encounterRepository.save(encounter);
    }

    /**
     * Checks if all players or all enemies in an encounter are defeated.
     */
    @Transactional(readOnly = true)
    public boolean checkEncounterCompletion(Long encounterId) {
        List<Combatant> combatants = combatantRepository.findByEncounterIdOrderByInitiativeDesc(encounterId);

        boolean hasPlayers = combatants.stream().anyMatch(Combatant::getIsPlayer);
        boolean hasEnemies = combatants.stream().anyMatch(c -> !c.getIsPlayer());

        if (!hasPlayers || !hasEnemies) {
            return false; // Can't complete if missing a side entirely, unless intended. Let's say false.
            // Wait, if no enemies, encounter is completed.
        }

        boolean allPlayersDefeated = combatants.stream()
                .filter(Combatant::getIsPlayer)
                .allMatch(Combatant::getIsDefeated);

        boolean allEnemiesDefeated = combatants.stream()
                .filter(c -> !c.getIsPlayer())
                .allMatch(Combatant::getIsDefeated);

        return allPlayersDefeated || allEnemiesDefeated;
    }

    /**
     * Executes the current turn if it belongs to an enemy.
     * @return A message describing the action taken, or null if player's turn.
     */
    @Transactional
    public String executeCurrentTurn(Long encounterId) {
        Encounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Encounter not found"));

        if (encounter.getStatus() != EncounterStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Encounter is not active.");
        }

        Long activeCombatantId = encounter.getActiveCombatantId();
        if (activeCombatantId == null) {
            return null;
        }

        Combatant activeCombatant = combatantRepository.findById(activeCombatantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active combatant not found"));

        if (!activeCombatant.getIsPlayer() && !activeCombatant.getIsDefeated()) {
            return enemyBehaviorService.executeEnemyTurn(encounterId, activeCombatantId);
        }

        return null;
    }
}
