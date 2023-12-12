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


    @Data @EqualsAndHashCode @AllArgsConstructor
    private static class Result {
        private String currentLine;
        private List<Integer> validation;
    }

    private static Map<Result, Long> cache = new HashMap<>();

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getLines().stream()
            .mapToLong(this::startCount)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return solvePart1(input.mapLines(this::unfold));
    }

    private String unfold(String line) {
        String[] parts = line.split(" ");
        String spring = (parts[0] + "?").repeat(5);
        spring = spring.substring(0, spring.length() - 1);

        String ints = (parts[1] + ",").repeat(5);
        ints = ints.substring(0, ints.length() - 1);

        return spring + " " + ints;
    }

    private long startCount(String line) {
        cache = new HashMap<>();

        String input = shorten(line.split(" ")[0]);
        List<Integer> validation = Arrays.stream(line.split(" ")[1].split(","))
            .mapToInt(Integer::parseInt)
            .boxed().toList();

        return getCount(input, validation);
    }

    private long getCount(String line, List<Integer> validation) {
        if(cache.containsKey(new Result(line, validation))) {
            return cache.get(new Result(line, validation));
        }

        if(line.isEmpty()) return validation.isEmpty()? 1: 0;
        if(line.charAt(0) == '.') return getCount(line.substring(1), validation);
        if(line.charAt(0) == '?') return getCount('.' + line.substring(1), validation) + getCount('#' + line.substring(1), validation);
        if(validation.isEmpty()) return 0;

        int groupSize = validation.get(0);
        if(groupSize > line.length() || !line.substring(0, groupSize).chars().allMatch(c -> c == '#' || c == '?')) {
            return 0;
        }

        long out = 0;
        if(groupSize == line.length()) {
            if(validation.size() == 1) out = 1;
        } else if(line.charAt(groupSize) == '.') {
            out = getCount(line.substring(groupSize + 1), validation.subList(1, validation.size()));
        }else if(line.charAt(groupSize) == '?') {
            out = getCount(line.substring(groupSize + 1), validation.subList(1, validation.size()));
        }

        cache.put(new Result(line, validation), out);
        return out;
    }

    private String shorten(String line) {
        return line.replace('.', ' ')
            .trim().replaceAll(" +", " ")
            .replace(' ', '.');
    }
}
