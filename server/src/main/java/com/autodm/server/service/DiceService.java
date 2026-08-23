package com.autodm.server.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DiceService {

    private final SecureRandom random = new SecureRandom();
    private static final Pattern DICE_PATTERN = Pattern.compile("(\\d*)d(\\d+)(?:\\s*([+-])\\s*(\\d+))?");

    public int roll(int sides) {
        if (sides < 1) {
            throw new IllegalArgumentException("Dice must have at least 1 side");
        }
        return random.nextInt(sides) + 1;
    }

    public RollResult roll(int numDice, int sides, int modifier) {
        if (numDice < 1) {
            throw new IllegalArgumentException("Must roll at least 1 die");
        }
        if (sides < 1) {
            throw new IllegalArgumentException("Dice must have at least 1 side");
        }

        List<Integer> rolls = new ArrayList<>(numDice);
        int total = 0;
        for (int i = 0; i < numDice; i++) {
            int roll = roll(sides);
            rolls.add(roll);
            total += roll;
        }

        total += modifier;
        return new RollResult(total, rolls, modifier);
    }

    public RollResult roll(String notation) {
        Matcher matcher = DICE_PATTERN.matcher(notation.toLowerCase().trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid dice notation: " + notation);
        }

        String countStr = matcher.group(1);
        int count = countStr.isEmpty() ? 1 : Integer.parseInt(countStr);

        int sides = Integer.parseInt(matcher.group(2));

        int modifier = 0;
        if (matcher.group(3) != null && matcher.group(4) != null) {
            modifier = Integer.parseInt(matcher.group(4));
            if ("-".equals(matcher.group(3))) {
                modifier = -modifier;
            }
        }

        return roll(count, sides, modifier);
    }

    public int rollD4() {
        return roll(4);
    }

    public int rollD6() {
        return roll(6);
    }

    public int rollD8() {
        return roll(8);
    }

    public int rollD10() {
        return roll(10);
    }

    public int rollD12() {
        return roll(12);
    }

    public int rollD20() {
        return roll(20);
    }

    public int rollPercentile() {
        // 1 to 100
        return roll(100);
    }
}
