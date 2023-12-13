package dev.phoenixofforce.adventofcode.y2023.day13;

import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class Day13_2023 implements Puzzle {

    private static boolean part2 = false;

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getParagraphs().stream()
            .mapToLong(this::findReflection)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        part2 = true;
        return solvePart1(input);
    }

    private long findReflection(String paragraph) {
        List<String> pattern = List.of(paragraph.split(" "));
        long left = findLeftCount(pattern);
        long top = findTopCount(pattern);

        return left + 100 * top;
    }

    private long findLeftCount(List<String> pattern) {
        for(int i = 0; i < pattern.get(0).length() -1; i++) {
            String current = getColumn(pattern, i);
            String next = getColumn(pattern, i + 1);

            long difference = calculateDifference(current, next);
            if((!part2 && current.equals(next)) || (part2 && difference <= 1)) {
                for(int offsetI = 1; offsetI < pattern.size(); offsetI++) {
                    int lowerIndex = i - offsetI;
                    int upperIndex = i + offsetI + 1;
                    if(lowerIndex < 0 || upperIndex >= pattern.get(0).length()) break;

                    String lower = getColumn(pattern, lowerIndex);
                    String upper = getColumn(pattern, upperIndex);
                    if (!lower.equals(upper)) {
                        difference += calculateDifference(lower, upper);
                        if(difference > 1) break;
                    }
                }

                if((difference == 0 && !part2) || (difference == 1 && part2)) return (i + 1);
            }
        }
        return 0;
    }

    private long findTopCount(List<String> pattern) {
        for(int i = 0; i < pattern.size() - 1; i++) {
            String current = pattern.get(i);
            String next = pattern.get(i + 1);

            long difference = calculateDifference(current, next);
            if((!part2 && current.equals(next)) || (part2 && difference <= 1)) {
                for(int offsetI = 1; offsetI < pattern.size(); offsetI++) {
                    int lowerIndex = i - offsetI;
                    int upperIndex = i + offsetI + 1;
                    if(lowerIndex < 0 || upperIndex >= pattern.size()) break;

                    String lower = pattern.get(lowerIndex);
                    String upper = pattern.get(upperIndex);
                    if (!lower.equals(upper)) {
                        difference += calculateDifference(lower, upper);
                        if(difference > 1) break;
                    }
                }

                if((difference == 0 && !part2) || (difference == 1 && part2)) return (i + 1);
            }
        }
        return 0;
    }

    private String getColumn(List<String> pattern, int index) {
        return pattern.stream()
            .map(e -> e.charAt(index) + "")
            .reduce("", (a,b) -> a + b);
    }

    private long calculateDifference(String a, String b) {
        return IntStream.range(0, a.length())
            .mapToObj(i -> a.charAt(i) == b.charAt(i))
            .filter(bool -> !bool)
            .count();
    }

}
