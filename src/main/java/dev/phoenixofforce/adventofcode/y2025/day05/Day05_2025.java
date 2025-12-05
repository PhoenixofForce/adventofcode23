package dev.phoenixofforce.adventofcode.y2025.day05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class Day05_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<long[]> ranges = Arrays.stream(input.getParagraphs().getFirst()
                .split(" "))
                .map(line -> line.split("-"))
                .map((strings -> new long[]{Long.parseLong(strings[0]), Long.parseLong(strings[1])}))
                .toList();

        List<Long> products = Arrays.stream(input.getParagraphs().get(1)
                .split(" "))
                .map(Long::parseLong)
                .toList();

        long freshProducts = 0;
        for(long product: products) {
            for(long[] range: ranges) {
                if(inRange(range, product)) {
                    freshProducts++;
                    break;
                }
            }
        }
        return freshProducts;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<long[]> ranges = Arrays.stream(input.getParagraphs().getFirst()
                        .split(" "))
                .map(line -> line.split("-"))
                .map((strings -> new long[]{Long.parseLong(strings[0]), Long.parseLong(strings[1]) + 1}))
                .sorted(Comparator.comparingLong(a -> a[0]))
                .toList();

        long freshProducts = 0;
        for(int i = 0; i < ranges.size(); i++) {
            long[] range = ranges.get(i);
            long upper = range[1];
            long lower = range[0];

            for(int j = 0; j < i; j++) {
                long[] otherRange = ranges.get(j);
                if(inRange(otherRange, lower)) lower = otherRange[1];
                if(inRange(otherRange, upper)) upper = otherRange[0];
                if(lower >= upper) break;
            }
            if(lower >= upper) continue;

            freshProducts += upper - lower;
        }
        return freshProducts;
    }

    private boolean inRange(long[] range, long value) {
        return range[0] <= value && value <= range[1];
    }

}
