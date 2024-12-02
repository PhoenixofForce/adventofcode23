package dev.phoenixofforce.adventofcode.y2024.day02;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day02_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getLines()
            .stream()
            .map(line -> Arrays.stream(line.split(" ")).map(Integer::parseInt).toList())
            .map(numbers -> this.isValid(numbers, -1))
            .filter(e -> e)
            .count();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return input.getLines()
            .stream()
            .map(line -> Arrays.stream(line.split(" ")).map(Integer::parseInt).toList())
            .map(numbers -> {
                if(isValid(numbers, -1)) return true;
                for(int i = 0; i < numbers.size(); i++) {
                    if(isValid(numbers, i)) return true;
                }
                return false;
            })
            .filter(e -> e)
            .count();
    }

    private boolean isValid(List<Integer> numbers, int skipIndex) {
        boolean isDecreasing = numbers.get(1) - numbers.get(0) < 0;
        if(skipIndex == 0) isDecreasing = numbers.get(2) - numbers.get(1) < 0;
        if(skipIndex == 1) isDecreasing = numbers.get(2) - numbers.get(0) < 0;

        int lastNumber = numbers.get(skipIndex == 0 ? 1 : 0);

        int start = 1;
        if(skipIndex == 0) start = 2;
        if(skipIndex == 1) start = 2;

        for(int i = start; i < numbers.size(); i++) {
            if(i == skipIndex) {
                continue;
            }

            int difference = numbers.get(i) - lastNumber;
            lastNumber = numbers.get(i);

            if(difference == 0) {
                return false;
            }

            if((isDecreasing && difference > 0) || (!isDecreasing && difference < 0)) {
                return false;
            }

            if(Math.abs(difference) > 3) {
                return false;
            }
        }
        return true;
    }
}
