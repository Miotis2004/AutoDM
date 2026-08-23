package com.autodm.server.service;

import com.autodm.server.model.Campaign;
import com.autodm.server.model.Combatant;
import com.autodm.server.model.CreatureTemplate;
import com.autodm.server.model.Encounter;
import com.autodm.server.model.EncounterDifficulty;
import com.autodm.server.model.EncounterStatus;
import com.autodm.server.model.Location;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CombatantRepository;
import com.autodm.server.repository.CreatureTemplateRepository;
import com.autodm.server.repository.EncounterRepository;
import com.autodm.server.repository.PlayerCharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class EncounterGenerationService {

    private final EncounterRepository encounterRepository;
    private final CombatantRepository combatantRepository;
    private final PlayerCharacterRepository playerCharacterRepository;
    private final CreatureTemplateRepository creatureTemplateRepository;
    private final DiceService diceService;
    private final Random random = new Random();

    public EncounterGenerationService(EncounterRepository encounterRepository,
                                      CombatantRepository combatantRepository,
                                      PlayerCharacterRepository playerCharacterRepository,
                                      CreatureTemplateRepository creatureTemplateRepository,
                                      DiceService diceService) {
        this.encounterRepository = encounterRepository;
        this.combatantRepository = combatantRepository;
        this.playerCharacterRepository = playerCharacterRepository;
        this.creatureTemplateRepository = creatureTemplateRepository;
        this.diceService = diceService;
    }

    @Transactional
    public Encounter createManualEncounter(Campaign campaign, Location location, String notes) {
        Encounter encounter = new Encounter();
        encounter.setCampaign(campaign);
        encounter.setLocation(location);
        encounter.setNotes(notes);
        encounter.setStatus(EncounterStatus.PENDING);
        return encounterRepository.save(encounter);
    }

    @Transactional
    public Encounter generateAutomatedEncounter(Campaign campaign, Location location, EncounterDifficulty difficulty) {
        Encounter encounter = createManualEncounter(campaign, location, "Automated generated encounter - " + difficulty);

        List<PlayerCharacter> party = playerCharacterRepository.findByCampaignId(campaign.getId());

        // Add players to the encounter
        int partyLevelSum = 0;
        for (PlayerCharacter pc : party) {
            Combatant playerCombatant = new Combatant();
            playerCombatant.setEncounter(encounter);
            playerCombatant.setName(pc.getName());
            playerCombatant.setHitPoints(pc.getHitPoints());
            playerCombatant.setMaxHitPoints(pc.getMaximumHitPoints());
            playerCombatant.setIsPlayer(true);
            playerCombatant.setPlayerCharacter(pc);
            combatantRepository.save(playerCombatant);

            partyLevelSum += (pc.getLevel() != null ? pc.getLevel() : 1);
        }

        // Generate enemy budget
        int budget = calculateBudget(partyLevelSum, difficulty);

        // Fetch available creature templates
        List<CreatureTemplate> templates = creatureTemplateRepository.findByCampaignId(campaign.getId());

        if (templates.isEmpty()) {
            return encounter; // No templates to spawn enemies from
        }

        List<Combatant> enemies = new ArrayList<>();
        int currentSpent = 0;

        // Very basic allocation: each enemy costs some generic budget (e.g. 1 per 10 HP, min 1)
        while (currentSpent < budget) {
            CreatureTemplate template = templates.get(random.nextInt(templates.size()));
            int cost = Math.max(1, template.getHitPoints() / 10);

            if (currentSpent + cost > budget && currentSpent > 0) {
                // To avoid infinite loop if no cheap templates exist, but allow at least one enemy if budget is small
                // Or try to find a cheaper one, for simplicity just break if we already spent something
                break;
            }

            Combatant enemyCombatant = new Combatant();
            enemyCombatant.setEncounter(encounter);

            // Handle duplicate names by adding a number or prefix
            int count = (int) enemies.stream().filter(e -> e.getTemplate() != null && e.getTemplate().getId().equals(template.getId())).count();
            String name = template.getName() + (count > 0 ? " " + (count + 1) : "");

            enemyCombatant.setName(name);
            enemyCombatant.setHitPoints(template.getHitPoints());
            enemyCombatant.setMaxHitPoints(template.getHitPoints());
            enemyCombatant.setIsPlayer(false);
            enemyCombatant.setTemplate(template);

            enemies.add(enemyCombatant);
            combatantRepository.save(enemyCombatant);

            currentSpent += cost;
        }

        return encounter;
    }

    private int calculateBudget(int partyLevelSum, EncounterDifficulty difficulty) {
        int baseBudget = Math.max(1, partyLevelSum);

        switch (difficulty) {
            case EASY:
                return baseBudget;
            case MEDIUM:
                return (int) (baseBudget * 1.5);
            case HARD:
                return baseBudget * 2;
            case DEADLY:
                return baseBudget * 3;
            default:
                return baseBudget;
        }
    }
}
