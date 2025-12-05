package dev.phoenixofforce.adventofcode.y2025.day03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.OptionalInt;

@Slf4j
@Component
public class Day03_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getLines()
                .stream()
                .mapToLong(line -> this.findJoltage(line, 2))
                .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return input.getLines()
                .stream()
                .mapToLong(line -> this.findJoltage(line, 12))
                .sum();
    }

    private long findJoltage(String line, int maxDigits) {
        String highestJoltage = findHighestNumber(line, maxDigits);
        log.info("{} => {}", line, highestJoltage);
        return Long.parseLong(highestJoltage);
    }

    private String findHighestNumber(String line, int maxDigits) {
        if(maxDigits == 0) return "";

        String searchSpace = line.substring(0, line.length() - maxDigits + 1);
        OptionalInt max = searchSpace.chars().max();
        if(max.isEmpty()) return "";

        char highestCharacter = (char) max.getAsInt();
        int index = searchSpace.indexOf(highestCharacter);
        return highestCharacter + findHighestNumber(line.substring(index + 1), maxDigits - 1);
    }

}
