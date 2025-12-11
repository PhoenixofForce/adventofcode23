package dev.phoenixofforce.adventofcode.y2025.day10;

import dev.phoenixofforce.adventofcode.meta.ProgressLogger;
import dev.phoenixofforce.adventofcode.solver.Dijkstra;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Slf4j
@Component
public class Day10_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Machine1> machines = input.getLines()
                .stream()
                .map(Machine1::new)
                .toList();

        long minPresses = 0;
        for(int i = 0; i < machines.size(); i++) {
            long presses = getMinimalPresses(machines.get(i));
            ProgressLogger.log(machines.size(), i, presses);
            minPresses += presses;
        }
        return minPresses;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Machine2> machines = input.getLines()
                .stream()
                .map(Machine2::new)
                .toList();

        long minPresses = 0;
        for(int i = 0; i < machines.size(); i++) {
            Optional<Long> presses = getMinimalPresses(machines.get(i));
            ProgressLogger.log(machines.size(), i, presses.orElse(0L));
            minPresses += presses.orElse(0L);
            cache.clear();
        }
        return minPresses;
    }

    private long getMinimalPresses(Machine1 machine) {
        Dijkstra.End<Machine1> end = Dijkstra.from(machine)
                .to(Machine1::isDone)
                .generateNextSteps(m -> {
                    List<Machine1> nextSteps = new ArrayList<>();
                    for(int i = 0; i < m.getButtons().size(); i++) {
                        if((m.getPressedButtons() & 1<<i) > 0) continue;
                        Machine1 next = pressButton(m, i);
                        nextSteps.add(next);
                    }
                    return nextSteps;
                })
                .getFirst();

        if(end.isEmpty()) return 0;
        return end.getDistance();
    }

    private Machine1 pressButton(Machine1 machine, int button) {
        Machine1 out = new Machine1(machine);
        out.setPressedButtons(out.getPressedButtons() | 1<<button);;
        for(int i: machine.getButtons().get(button)) {
            out.toggleLight(i);
        }
        return out;
    }

    private Map<Integer, Optional<Long>> cache = new HashMap<>();
    private Optional<Long> getMinimalPresses(Machine2 machine) {
        if(!machine.isSolvable()) return Optional.empty();
        if(machine.isDone()) return Optional.of(1L);
        if(cache.containsKey(machine.hashCode())) return cache.get(machine.hashCode());

        Optional<Long> minPresses = Optional.empty();
        for(int i = 0; i < machine.getButtons().size(); i++) {
            Machine2 next = pressButton(machine, i);
            Optional<Long> presses = getMinimalPresses(next);
            if(presses.isPresent()) minPresses = Optional.of(Math.min(minPresses.orElse(Long.MAX_VALUE), presses.get() + 1));
        }
        cache.put(machine.hashCode(), minPresses);
        return minPresses;
    }

    private Machine2 pressButton(Machine2 machine, int button) {
        Machine2 out = new Machine2(machine);
        for(int i: machine.getButtons().get(button)) {
            out.increaseJoltage(i);
        }
        return out;
    }


}
