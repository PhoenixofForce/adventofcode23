package dev.phoenixofforce.adventofcode.y2023.day08;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day08_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        Map<String, String> route = input.getLines().stream()
            .filter(s -> s.contains("="))
            .collect(Collectors.toMap(s -> s.split("=")[0].trim(),
                s -> s.split("=")[1].trim().replaceAll("[() ]", "")));

        int steps = 0;
        String current = "AAA";
        while(!"ZZZ".equals(current)) {
            for(char c: input.getLines().get(0).toCharArray()) {
                String[] options = route.get(current).split(",");
                current = options[c == 'L'? 0: 1];
                steps++;
                if("ZZZ".equals(current)) return steps;
            }
        }

        return steps;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Map<String, String> route = input.getLines().stream()
            .filter(s -> s.contains("="))
            .collect(Collectors.toMap(s -> s.split("=")[0].trim(),
                s -> s.split("=")[1].trim().replaceAll("[() ]", "")));

        List<String> ghostRoutes = new ArrayList<>(route.keySet().stream()
            .filter(s -> s.endsWith("A"))
            .toList());

        List<Long> allSteps = new ArrayList<>();
        for(String start: ghostRoutes) {
            long steps = 0;
            String current = start;
            while(!current.endsWith("Z")) {
                for(char c: input.getLines().get(0).toCharArray()) {
                    String[] options = route.get(current).split(",");
                    current = options[c == 'L'? 0: 1];
                    steps++;
                    if(current.endsWith("Z")) {
                        allSteps.add(steps);
                    }
                }
            }
        }

        long out = 1;
        for(long l: allSteps) {
            long gcd = gcd(out, l);
            out = (out * l) / gcd;
        }

        return out;
    }

    long gcd(long n1, long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcd(n2, n1 % n2);
    }

}
