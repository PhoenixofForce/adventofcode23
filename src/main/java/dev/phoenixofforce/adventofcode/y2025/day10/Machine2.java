package dev.phoenixofforce.adventofcode.y2025.day10;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Machine2 {

    private final int[] joltage;
    private final int[] joltageGoal;
    private List<List<Integer>> buttons = new ArrayList<>();

    public Machine2(String line) {
        String[] parts = line.split(" ");

        for(int i = 1; i < parts.length - 1; i++) {
            String part = parts[i].substring(1, parts[i].length() - 1);
            List<Integer> button = Arrays.stream(part.split(","))
                    .map(Integer::parseInt)
                    .toList();

            buttons.add(button);
        }

        List<Integer> joltageProxy = Arrays.stream(parts[parts.length - 1].substring(1, parts[parts.length - 1].length() - 1).split(","))
                .map(Integer::parseInt).toList();
        joltage = new int[joltageProxy.size()];
        joltageGoal = new int[joltageProxy.size()];
        for(int i = 0; i < joltageProxy.size(); i++) {
            joltageGoal[i] = joltageProxy.get(i);
            joltage[i] = 0;
        }
    }

    public Machine2(Machine2 other) {
        this.joltage = other.joltage.clone();
        this.joltageGoal = other.joltageGoal;
        this.buttons = other.buttons;
    }

    public boolean isDone() {
        for(int i = 0; i < joltage.length; i++) {
            if(joltage[i] != joltageGoal[i]) return false;
        }
        return true;
    }

    public boolean isSolvable() {
        for(int i = 0; i < joltage.length; i++) {
            if(joltage[i] > joltageGoal[i]) {
                return false;
            };
        }
        return true;
    }

    public void increaseJoltage(int i) {
        joltage[i] = joltage[i] + 1;
    }

    public int heuristic() {
        int out = 0;
        for(int i = 0; i < joltage.length; i++) {
            out += joltageGoal[i] - joltage[i];
        }
        return out * out * out;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Machine2 m)) {
            return false;
        }
        for(int i = 0; i < joltage.length; i++) {
            if(joltage[i] != m.joltage[i]) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(joltage);
    }
}
