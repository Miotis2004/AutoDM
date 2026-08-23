package com.autodm.server.service;

public class AttackResult {
    private final boolean isHit;
    private final int attackTotal;
    private final int damageApplied;
    private final boolean targetDefeated;
    private final String description;

    public AttackResult(boolean isHit, int attackTotal, int damageApplied, boolean targetDefeated, String description) {
        this.isHit = isHit;
        this.attackTotal = attackTotal;
        this.damageApplied = damageApplied;
        this.targetDefeated = targetDefeated;
        this.description = description;
    }

    public boolean isHit() {
        return isHit;
    }

    public int getAttackTotal() {
        return attackTotal;
    }

    public int getDamageApplied() {
        return damageApplied;
    }

    public boolean isTargetDefeated() {
        return targetDefeated;
    }

    public String getDescription() {
        return description;
    }
}
