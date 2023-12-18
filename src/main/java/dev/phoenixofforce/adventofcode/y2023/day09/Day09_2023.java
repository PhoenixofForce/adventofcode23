package dev.phoenixofforce.adventofcode.y2023.day09;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class Day09_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getLines().stream()
            .mapToLong(l -> extrapolate(l, true))
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return input.getLines().stream()
            .mapToLong(l -> extrapolate(l, false))
            .sum();
    }

    private long extrapolate(String line, boolean getLast) {
        List<Long> values = new ArrayList<>(Arrays.stream(line.split(" "))
            .mapToLong(Long::parseLong)
            .boxed().toList());

        List<List<Long>> derivatives = new ArrayList<>();
        derivatives.add(values);
        List<Long> latest = values;
        while(latest.stream().anyMatch(e -> e != 0)) {
            List<Long> derivative = new ArrayList<>();
            for(int i = 0; i < latest.size() - 1; i++) derivative.add(latest.get(i + 1) - latest.get(i));
            derivatives.add(derivative);
            latest = derivative;

        }

        latest.add(0L);
        for(int i = derivatives.size() - 2; i >= 0; i--) {
            long nextValue = derivatives.get(i).getLast() + derivatives.get(i + 1).getLast();
            long prevValue = derivatives.get(i).getFirst() - derivatives.get(i + 1).getFirst();

            System.out.println(prevValue + " <= " + derivatives.get(i) + " => " + nextValue);
            derivatives.get(i).add(nextValue);
            derivatives.get(i).add(0, prevValue);
        }
        System.out.println();
        return getLast? derivatives.get(0).getLast(): derivatives.get(0).getFirst();
    }
}
