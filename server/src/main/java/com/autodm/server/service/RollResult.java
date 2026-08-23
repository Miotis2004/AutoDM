package com.autodm.server.service;

import java.util.List;

public class RollResult {
    private final int total;
    private final List<Integer> rolls;
    private final int modifier;

    public RollResult(int total, List<Integer> rolls, int modifier) {
        this.total = total;
        this.rolls = rolls;
        this.modifier = modifier;
    }

    public int getTotal() {
        return total;
    }

    public List<Integer> getRolls() {
        return rolls;
    }

    public int getModifier() {
        return modifier;
    }
}
