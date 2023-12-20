package dev.phoenixofforce.adventofcode.y2023.day20;

import dev.phoenixofforce.adventofcode.solver.Maths;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day20_2023 implements Puzzle {

    private static boolean part2 = false;
    private static long button;
    private static Map<String, Long> buttonTurnCache = new HashMap<>();

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Pulse {
        private String sender;
        private String receiver;
        private boolean isHigh;
    }

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Machine {
        private char prefix = ' ';
        private String name;
        private List<String> destinations;

        public Machine(String line) {
            String[] parts = line.split(" -> ");
            this.name = parts[0];
            if(!('A' <= this.name.charAt(0) && this.name.charAt(0) <= 'z')) {
                this.prefix = this.name.charAt(0);
                this.name = this.name.substring(1);
            }
            this.destinations = Arrays.stream(parts[1].split(","))
                .map(String::trim)
                .toList();
        }
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        Map<String, Map<String, Boolean>> conjunctionCache = new HashMap<>();
        Map<String, Boolean> flipFlopCache = new HashMap<>();

        Map<String, Machine> machines = input.getLines().stream()
            .map(Machine::new)
            .collect(Collectors.toMap(Machine::getName, e -> e));

        for(Machine m: machines.values()) {
            if(m.getPrefix() == '&') conjunctionCache.put(m.getName(), new HashMap<>());
            else if (m.getPrefix() == '%') flipFlopCache.put(m.getName(), false);
        }

        for(Machine m: machines.values()) {
            for(String destination: m.getDestinations()) {
                if(conjunctionCache.containsKey(destination)) {
                    conjunctionCache.get(destination).put(m.getName(), false);
                }
            }
        }

        long lows = 0;
        long highs = 0;
        for(int i = 0; i < 1000; i++) {
            long[] out = pushButton(machines, conjunctionCache, flipFlopCache);
            lows += out[0];
            highs += out[1];
        }

        System.out.println("Total Count: " + lows + " " + highs);
        return lows * highs;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        part2 = true;
        Map<String, Map<String, Boolean>> conjunctionCache = new HashMap<>();
        Map<String, Boolean> flipFlopCache = new HashMap<>();

        Map<String, Machine> machines = input.getLines().stream()
            .map(Machine::new)
            .collect(Collectors.toMap(Machine::getName, e -> e));

        for(Machine m: machines.values()) {
            if(m.getPrefix() == '&') conjunctionCache.put(m.getName(), new HashMap<>());
            else if (m.getPrefix() == '%') flipFlopCache.put(m.getName(), false);
        }

        for(Machine m: machines.values()) {
            for(String destination: m.getDestinations()) {
                if(conjunctionCache.containsKey(destination)) {
                    conjunctionCache.get(destination).put(m.getName(), false);
                }
            }
        }

        button = 1;
        while(true) {
            long[] out = pushButton(machines, conjunctionCache, flipFlopCache);
            if(out.length == 0) return (button);
            button++;
        }
    }

    private long[] pushButton(Map<String, Machine> machines, Map<String, Map<String, Boolean>> conjunctionCache,
                Map<String, Boolean> flipFlopCache) {

        long lowPulses = 0;
        long highPulses = 0;

        List<Pulse> pulses = new ArrayList<>();
        pulses.add(new Pulse("button", "broadcaster", false));

        while(!pulses.isEmpty()) {

            Pulse toHandle = pulses.remove(0);
            if(toHandle.isHigh) highPulses++;
            else lowPulses++;

            //System.out.println(toHandle.sender + " -" + (toHandle.isHigh? "high": "low") + "-> " + toHandle.receiver);

            if(part2 && toHandle.receiver.equals("rx") && !toHandle.isHigh) {
                return new long[]{};
            }

            Machine receiver = machines.get(toHandle.getReceiver());
            if(receiver == null) continue;

            if(receiver.getName().equals("broadcaster")) {
                receiver.getDestinations()
                    .forEach(destination -> {
                        pulses.add(new Pulse(receiver.name, destination, toHandle.isHigh));
                    });
            } else if(receiver.getPrefix() == '%') {
                if(!toHandle.isHigh) {
                    flipFlopCache.put(receiver.name, !flipFlopCache.get(receiver.name));
                    boolean stateNow = flipFlopCache.get(receiver.getName());

                    receiver.getDestinations()
                        .forEach(destination -> {
                            pulses.add(new Pulse(receiver.name, destination, stateNow));
                        });
                }
            } else if(receiver.getPrefix() == '&') {
                conjunctionCache.get(receiver.name)
                    .put(toHandle.sender, toHandle.isHigh);

                boolean anyLow = conjunctionCache.get(receiver.name).values().stream()
                    .anyMatch(e -> !e);

                receiver.getDestinations()
                    .forEach(destination -> {
                        pulses.add(new Pulse(receiver.name, destination, anyLow));
                    });

                if(part2 && toHandle.receiver.equals("vr") && conjunctionCache.get(receiver.name).get(toHandle.sender)) {
                    buttonTurnCache.put(toHandle.getSender(), button);
                    if(buttonTurnCache.size() == 4) {
                        button = Maths.lcm(buttonTurnCache.values());
                        return new long[]{};
                    }
                }
            }
        }
        return new long[]{lowPulses, highPulses};
    }

}
