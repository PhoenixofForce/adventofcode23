package dev.phoenixofforce.adventofcode.y2025.day02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class Day02_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        long counter = 0;

        List<long[]> ranges = Arrays.stream(input.getFile().split(","))
                .map(e -> new long[]{
                        Long.parseLong(e.split("-")[0]),
                        Long.parseLong(e.split("-")[1])
                })
                .toList();

        for(long[] range: ranges) {
            long lower = range[0];
            long upper = range[1];
            for(long current = lower; current <= upper; current++) {
                String asString = current + "";
                if(asString.matches("^(\\d+)\\1$")) {
                    log.info(asString);
                    counter += current;
                }
            }
        }

        return counter;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long counter = 0;

        List<long[]> ranges = Arrays.stream(input.getFile().split(","))
                .map(e -> new long[]{
                        Long.parseLong(e.split("-")[0]),
                        Long.parseLong(e.split("-")[1])
                })
                .toList();

        for(long[] range: ranges) {
            long lower = range[0];
            long upper = range[1];
            for(long current = lower; current <= upper; current++) {
                String asString = current + "";
                if(asString.matches("^(\\d+)\\1+$")) {
                    log.info(asString);
                    counter += current;
                }
            }
        }

        return counter;
    }

}
