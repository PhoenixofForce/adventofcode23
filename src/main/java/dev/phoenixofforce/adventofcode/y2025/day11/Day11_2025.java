package dev.phoenixofforce.adventofcode.y2025.day11;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Slf4j
@Component
public class Day11_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        Map<String, List<String>> graph = new HashMap<>();
        for(String line: input.getLines()) {
            String from = line.split(":")[0];
            List<String> to = Arrays.stream(line.split(":")[1].trim().split(" ")).toList();
            graph.put(from, to);
        }

        return countPaths(graph, "you", "out");
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Map<String, List<String>> graph = new HashMap<>();
        for(String line: input.getLines()) {
            String from = line.split(":")[0];
            List<String> to = Arrays.stream(line.split(":")[1].trim().split(" ")).toList();
            graph.put(from, to);
        }

        long out = 0;

        long fromDac = countPaths(graph, "dac", "fft");
        if(fromDac > 0) {
            long toDac = countPaths(graph, "svr", "dac");
            long toOut = countPaths(graph, "fft", "out");
            out += fromDac * toDac * toOut;
        }

        long fromFft = countPaths(graph, "fft", "dac");
        if(fromFft > 0) {
            long toFft = countPaths(graph, "svr", "fft");
            long toOut = countPaths(graph, "dac", "out");
            out += fromFft * toFft * toOut;
        }


        return out;
    }


    private long countPaths(Map<String, List<String>> graph, String start, String goal) {
        return countPaths(graph, start, goal, new HashMap<>(), new HashSet<>());
    }

    private long countPaths(Map<String, List<String>> graph, String current, String goal, Map<String, Long> cache, Set<String> visited) {
        if (current.equals(goal)) return 1;
        if (cache.containsKey(current)) return cache.get(current);
        if (visited.contains(current)) return 0;

        visited.add(current);
        long count = 0;
        for (String next : graph.getOrDefault(current, List.of())) {
            count += countPaths(graph, next, goal, cache, visited);
        }
        visited.remove(current);

        cache.put(current, count);
        return count;
    }


}
