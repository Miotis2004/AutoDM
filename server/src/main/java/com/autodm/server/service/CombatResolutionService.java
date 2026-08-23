package com.autodm.server.service;

import com.autodm.server.model.CampaignEvent;
import com.autodm.server.model.CampaignEventType;
import com.autodm.server.model.Combatant;
import com.autodm.server.model.PlayerCharacter;
import com.autodm.server.repository.CampaignEventRepository;
import com.autodm.server.repository.CombatantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CombatResolutionService {

    private final DiceService diceService;
    private final CombatantRepository combatantRepository;
    private final CampaignEventRepository campaignEventRepository;

    public CombatResolutionService(DiceService diceService,
                                   CombatantRepository combatantRepository,
                                   CampaignEventRepository campaignEventRepository) {
        this.diceService = diceService;
        this.combatantRepository = combatantRepository;
        this.campaignEventRepository = campaignEventRepository;
    }

    @Transactional
    public AttackResult resolveAttack(Combatant attacker, Combatant target, int attackBonus, String damageNotation) {
        int targetAc = 10;
        if (Boolean.TRUE.equals(target.getIsPlayer()) && target.getPlayerCharacter() != null && target.getPlayerCharacter().getArmorClass() != null) {
            targetAc = target.getPlayerCharacter().getArmorClass();
        } else if (Boolean.FALSE.equals(target.getIsPlayer()) && target.getTemplate() != null && target.getTemplate().getArmorClass() != null) {
            targetAc = target.getTemplate().getArmorClass();
        }

        RollResult attackRollResult = diceService.roll(1, 20, attackBonus);
        int attackTotal = attackRollResult.getTotal();

        StringBuilder sb = new StringBuilder();
        sb.append(attacker.getName()).append(" attacks ").append(target.getName()).append(". ");
        sb.append("Roll: ").append(attackRollResult.getRolls().get(0)).append(" + ").append(attackBonus).append(" = ").append(attackTotal).append(". ");

        boolean isHit = attackTotal >= targetAc;
        int damageApplied = 0;
        boolean targetDefeated = false;

        if (isHit) {
            RollResult damageRollResult;
            try {
                damageRollResult = diceService.roll(damageNotation);
            } catch (Exception e) {
                // Fallback if notation is invalid
                damageRollResult = diceService.roll(1, 4, 0);
            }
            damageApplied = damageRollResult.getTotal();

            sb.append("Hit! ").append("Deals ").append(damageApplied).append(" damage.");

            int newHp = target.getHitPoints() - damageApplied;
            target.setHitPoints(newHp);

            if (newHp <= 0) {
                target.setHitPoints(0);
                target.setIsDefeated(true);
                targetDefeated = true;
                sb.append(" ").append(target.getName()).append(" is defeated!");
                if (Boolean.TRUE.equals(target.getIsPlayer()) && target.getPlayerCharacter() != null) {
                    target.getPlayerCharacter().setIsUnconscious(true);
                }
            }
            combatantRepository.save(target);
        } else {
            sb.append("Misses (AC ").append(targetAc).append(").");
        }

        if (attacker.getEncounter() != null && attacker.getEncounter().getCampaign() != null) {
            CampaignEventType eventType = isHit ? CampaignEventType.DAMAGE : CampaignEventType.COMBAT;
            campaignEventRepository.save(new CampaignEvent(attacker.getEncounter().getCampaign(), eventType, sb.toString()));
        }

        return new AttackResult(isHit, attackTotal, damageApplied, targetDefeated, sb.toString());
    }
}
