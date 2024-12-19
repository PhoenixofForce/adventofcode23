package dev.phoenixofforce.adventofcode.y2024.day19;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day19_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<String> availableTowels = Arrays.stream(input.getLines().getFirst().split(", ")).toList();

        return input.getLines().stream()
            .skip(2)
            .map(e -> waysPossible(availableTowels, e))
            .filter(e -> e > 0)
            .count();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<String> availableTowels = Arrays.stream(input.getLines().getFirst().split(", ")).toList();

        return input.getLines().stream()
            .skip(2)
            .mapToLong(e -> waysPossible(availableTowels, e))
            .sum();
    }

    private final Map<String, Long> cache = new HashMap<>();
    private long waysPossible(List<String> availableTowels, String pattern) {
        if(pattern.isEmpty()) return 1;
        if(cache.containsKey(pattern)) return cache.get(pattern);

        long out = 0;
        for(String towel: availableTowels) {
            if(pattern.startsWith(towel)) {
                out += waysPossible(availableTowels, pattern.substring(towel.length()));
            }
        }

        cache.put(pattern, out);
        return out;
    }
}
