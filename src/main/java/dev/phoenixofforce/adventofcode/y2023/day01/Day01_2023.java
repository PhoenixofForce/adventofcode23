package dev.phoenixofforce.adventofcode.y2023.day01;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

@Component
public class Day01_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getLines()
            .stream()
            .map(s -> s.replaceAll("[A-z]", ""))
            .filter(s -> !s.isEmpty())
            .map(s -> "" + s.charAt(0) + s.charAt(s.length() - 1))
            .mapToInt(Integer::parseInt)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return solvePart1(input.mapLines(line -> line.replaceAll("one", "o1e").
            replaceAll("two", "t2o").
            replaceAll("three", "t3e").
            replaceAll("four", "f4r").
            replaceAll("five", "f5e").
            replaceAll("six", "s6x").
            replaceAll("seven", "s7n").
            replaceAll("eight", "e8t").
            replaceAll("nine", "n9e")
        ));
    }

}
