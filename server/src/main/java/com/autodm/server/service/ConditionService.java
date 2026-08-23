package com.autodm.server.service;

import com.autodm.server.model.Combatant;
import com.autodm.server.model.Condition;
import com.autodm.server.repository.ConditionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConditionService {

    private final ConditionRepository conditionRepository;

    public ConditionService(ConditionRepository conditionRepository) {
        this.conditionRepository = conditionRepository;
    }

    /**
     * Applies a new condition to a combatant.
     */
    @Transactional
    public Condition applyCondition(Combatant combatant, String name, String description, Integer duration, String source) {
        Condition condition = new Condition();
        condition.setCombatant(combatant);
        condition.setName(name);
        condition.setDescription(description);
        condition.setDuration(duration);
        condition.setSource(source);
        condition.setIsActive(true);
        return conditionRepository.save(condition);
    }

    /**
     * Deactivates a condition by its ID.
     */
    @Transactional
    public Optional<Condition> deactivateCondition(Long conditionId) {
        Optional<Condition> optionalCondition = conditionRepository.findById(conditionId);
        if (optionalCondition.isPresent()) {
            Condition condition = optionalCondition.get();
            condition.setIsActive(false);
            return Optional.of(conditionRepository.save(condition));
        }
        return Optional.empty();
    }

    /**
     * Gets all active conditions for a specific combatant.
     */
    public List<Condition> getActiveConditions(Long combatantId) {
        return conditionRepository.findByCombatantId(combatantId)
                .stream()
                .filter(Condition::getIsActive)
                .collect(Collectors.toList());
    }

    /**
     * Decrements the duration of all active conditions with a duration for a specific combatant.
     * Deactivates conditions that reach a duration of 0.
     */
    @Transactional
    public void processTurnEnd(Long combatantId) {
        List<Condition> activeConditions = getActiveConditions(combatantId);
        for (Condition condition : activeConditions) {
            if (condition.getDuration() != null) {
                condition.setDuration(condition.getDuration() - 1);
                if (condition.getDuration() <= 0) {
                    condition.setIsActive(false);
                }
                conditionRepository.save(condition);
            }
        }
    }
}
