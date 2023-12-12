package dev.phoenixofforce.adventofcode.y2023.day12;

import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.Tree;
import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Component
public class Day12_2023 implements Puzzle {

    private int solved = 1;

    @Data @EqualsAndHashCode @AllArgsConstructor
    private static class Result {
        boolean matches;
        private String shortVersion;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getLines().stream()
            //.parallel()
            .mapToLong(this::getConfigurations)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return "";//solvePart1(input.mapLines(this::unfold));
    }

    private String unfold(String line) {
        String[] parts = line.split(" ");
        String spring = (parts[0] + "?").repeat(5);
        spring = spring.substring(0, spring.length() - 1);

        String ints = (parts[1] + ",").repeat(5);
        ints = ints.substring(0, ints.length() - 1);

        return spring + " " + ints;
    }

    private long getConfigurations(String line) {
        List<Integer> validation = Arrays.stream(line.split(" ")[1].split(","))
            .mapToInt(Integer::parseInt)
            .boxed()
            .toList();

        int total = validation.stream().mapToInt(e -> e).sum();
        String input = line.split(" ")[0];
        if(input.length() == total + validation.size() - 1) {
            System.out.println(solved++ + " " + input + " " + 1);
            return 1;
        }

        AtomicLong out = new AtomicLong();
        getConfigurations(input, validation, out);
        System.out.println(solved++ + " " + line + " " + out.get());
        return out.get();
    }

    private void getConfigurations(String line, List<Integer> validation, AtomicLong count) {
        if(!line.contains("?")) {
            Result finalMatch = matches(line, validation);
            if(finalMatch.matches) {
                count.incrementAndGet();
            }
            return;
        }

        Result result = matches(line, validation);
        if(!result.matches) {
            return;
        }

        getConfigurations(line.replaceFirst("\\?", "."), validation, count);
        getConfigurations(line.replaceFirst("\\?", "#"), validation, count);
    }

    private Result matches(String line, List<Integer> validation) {
        if(line.replace(".", "").length() < validation.stream().mapToInt(e -> e).sum()) return new Result(false, line);

        int validationIndex = 0;
        int currentDefectCount = 0;
        char lastChar = ' ';

        for(int i = 0; i < line.toCharArray().length; i++) {
            char c = line.charAt(i);

            if(c == '?') {
                if(lastChar == '.') return new Result(true, shorten(line));
                return new Result(validationIndex >= validation.size() || currentDefectCount <= validation.get(validationIndex), shorten(line));
            }
            if(c == '#') currentDefectCount++;
            if(c == '.' && lastChar == '#') {
                if(validationIndex >= validation.size() ||
                    currentDefectCount != validation.get(validationIndex)) return new Result(false, line);
                currentDefectCount = 0;
                validationIndex++;
            }

            lastChar = c;
        }

        if(lastChar == '#') {
            if(validationIndex >= validation.size() ||
                currentDefectCount != validation.get(validationIndex)) return new Result(false, line);
            validationIndex++;
        }

        if(validationIndex != validation.size()) {
            return new Result(false, line);
        }

        return new Result(true, shorten(line));
    }

    private String shorten(String line) {
        return line.replace('.', ' ')
            .trim().replaceAll(" +", " ")
            .replace(' ', '.');
    }
}
