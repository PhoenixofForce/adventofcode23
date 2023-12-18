package dev.phoenixofforce.adventofcode.y2021.day01;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Day01_2021 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Integer> depths = input.getLines()
                .stream()
                .map(Integer::parseInt)
                .toList();

        List<Integer> changes = new ArrayList<>();

        for(int i = 1; i < depths.size(); i++) {
            int pre = depths.get(i-1);
            int cur = depths.get(i);

            changes.add((int) Math.signum(cur-pre));
        }
        return changes.stream().filter(i -> i > 0).count();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Integer> depths = input.getLines()
                .stream()
                .map(Integer::parseInt)
                .toList();

        List<Integer> changes = new ArrayList<>();
        for(int i = 2; i < depths.size(); i++) {
            if(i + 1 >= depths.size()) break;

            int pre = depths.get(i-2) + depths.get(i-1) + depths.get(i);
            int cur = depths.get(i-1) + depths.get(i) + depths.get(i+1);

            changes.add((int) Math.signum(cur-pre));
        }

        return changes.stream().filter(i -> i > 0).count();
    }

}
