package dev.phoenixofforce.adventofcode.y2024.day07;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day07_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        long out = 0;

        for(String s: input.getLines()) {
            long result = Long.parseLong(s.split(":")[0].trim());
            List<Long> numbers = Arrays.stream(s.split(":")[1].trim().split(" "))
                .map(e -> Long.parseLong(e.trim()))
                .toList();

            if(isPossible(result, numbers, false)) {
                out += result;
            }
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long out = 0;

        for(String s: input.getLines()) {
            long result = Long.parseLong(s.split(":")[0].trim());
            List<Long> numbers = Arrays.stream(s.split(":")[1].trim().split(" "))
                .map(e -> Long.parseLong(e.trim()))
                .toList();

            if(isPossible(result, numbers, true)) {
                out += result;
            }
        }

        return out;
    }

    private boolean isPossible(long result, List<Long> numbers, boolean part2) {
        if(numbers.size() == 1) return result == numbers.getFirst();
        if(numbers.getFirst() > result) return false;
        List<Long> copy = new ArrayList<>(numbers);

        long first = copy.removeFirst();
        long second = copy.removeFirst();

        long sum = first + second;
        copy.add(0, sum);
        boolean sumFulfills = isPossible(result, copy, part2);
        copy.removeFirst();
        if(sumFulfills) return true;

        long product = first * second;
        copy.add(0, product);
        boolean productFulfills = isPossible(result, copy, part2);
        copy.removeFirst();
        if(productFulfills) return true;

        if(!part2) return false;

        long concat = Long.parseLong(first + "" + second);
        copy.add(0, concat);
        boolean concatFulfills = isPossible(result, copy, part2);
        copy.removeFirst();
        if(concatFulfills) return true;

        return false;
    }

}
