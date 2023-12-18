package dev.phoenixofforce.adventofcode.y2023.day03;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Day03_2023 implements Puzzle {

    private final Map<Position, List<Integer>> gears = new HashMap<>();

    @Override
    public Object solvePart1(PuzzleInput input) {
        int sum = 0;

        for(int lineIndex = 0; lineIndex < input.getLines().size(); lineIndex++) {
            String line = input.getLines().get(lineIndex);

            int charIndex;
            String currentNumber = "";
            for(charIndex = 0; charIndex < line.length(); charIndex++) {
                char current = line.charAt(charIndex);

                if(Character.isDigit(current)) currentNumber += current;
                else if(!currentNumber.isEmpty()) {
                    boolean isPartNumber = isPartNumberAndFindGears(input, lineIndex, charIndex, currentNumber);
                    if(isPartNumber) {
                        sum += Integer.parseInt(currentNumber);
                    }
                    currentNumber = "";
                }
            }

            if(!currentNumber.isEmpty()) {
                boolean isPartNumber = isPartNumberAndFindGears(input, lineIndex, charIndex, currentNumber);
                if(isPartNumber) {
                    sum += Integer.parseInt(currentNumber);
                }
            }
        }

        return sum;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return gears.values().stream()
            .filter(integers -> integers.size() == 2)
            .mapToLong(integers -> integers.stream().reduce(1, (a, b) -> a * b))
            .sum();
    }

    private boolean isPartNumberAndFindGears(PuzzleInput input, int lineIndex, int charIndex, String currentNumber) {
        boolean isPartNumber = false;
        String line = input.getLines().get(lineIndex);

        for(int dx = -currentNumber.length() - 1; dx <= 0; dx++) {
            for(int dy = -1; dy <= 1; dy++) {
                if(lineIndex + dy < 0 || lineIndex + dy >= input.getLines().size()) continue;
                if(charIndex + dx < 0 || charIndex + dx >= line.length()) continue;

                char toCheck = input.getLines().get(lineIndex + dy)
                    .charAt(charIndex + dx);
                if(!Character.isDigit(toCheck) && toCheck != '.') isPartNumber = true;
                if(toCheck == '*') {
                    Position gear = new Position(charIndex + dx, lineIndex + dy);
                    if(!gears.containsKey(gear)) gears.put(gear, new ArrayList<>());
                    gears.get(gear).add(Integer.parseInt(currentNumber));
                }
            }
        }

        return isPartNumber;
    }

}
