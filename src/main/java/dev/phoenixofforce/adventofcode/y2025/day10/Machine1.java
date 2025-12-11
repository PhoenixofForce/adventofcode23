package dev.phoenixofforce.adventofcode.y2025.day10;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Machine1 {

    private final boolean[] lights;
    private final boolean[] lightsGoal;
    private List<List<Integer>> buttons = new ArrayList<>();
    private int pressedButtons = 0;

    public Machine1(String line) {
        String[] parts = line.split(" ");
        List<Boolean> lightsProxy = Arrays.stream(parts[0].substring(1, parts[0].length() - 1).split(""))
                .map(e -> e.equals("#")).toList();

        lights = new boolean[lightsProxy.size()];
        lightsGoal = new boolean[lightsProxy.size()];
        for(int i = 0; i < lightsProxy.size(); i++) {
            lightsGoal[i] = lightsProxy.get(i);
            lights[i] = false;
        }

        for(int i = 1; i < parts.length - 1; i++) {
            String part = parts[i].substring(1, parts[i].length() - 1);
            List<Integer> button = Arrays.stream(part.split(","))
                    .map(Integer::parseInt)
                    .toList();

            buttons.add(button);
        }
    }

    public Machine1(Machine1 other) {
        this.lights = other.lights.clone();
        this.lightsGoal = other.lightsGoal;
        this.buttons = other.buttons;
        this.pressedButtons = other.pressedButtons;
    }

    public boolean isDone() {
        for(int i = 0; i < lights.length; i++) {
            if(lights[i] != lightsGoal[i]) return false;
        }
        return true;
    }

    public void toggleLight(int i) {
        lights[i] = !lights[i];
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Machine1 m)) {
            return false;
        }
        return hashCode() == m.hashCode();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(lights);
    }
}
