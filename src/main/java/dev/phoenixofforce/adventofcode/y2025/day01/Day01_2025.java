package dev.phoenixofforce.adventofcode.y2025.day01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Slf4j
@Component
public class Day01_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        int currentNumber = 50;
        int zeroCounter = 0;

        for(String line: input.getLines()) {
            int offset = Integer.parseInt(line.substring(1));
            if(line.startsWith("L")) offset *= -1;

            currentNumber += offset;
            currentNumber %= 100;
            while(currentNumber < 0) currentNumber += 100;

            if(currentNumber == 0) zeroCounter++;
        }

        return zeroCounter;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        int currentNumber = 50;
        int zeroCounter = 0;
        final int stepSize = 100;

        for(String line: input.getLines()) {
            int maxOffset = Integer.parseInt(line.substring(1));
            boolean isGettingSmaller = line.startsWith("L");

            for(int i = 0; i < maxOffset; i += stepSize) {
                int offset = Math.min(stepSize, maxOffset - i);

                if(isGettingSmaller) offset *= -1;
                int newCurrentNumber = currentNumber + offset;
                newCurrentNumber %= 100;
                while(newCurrentNumber < 0) newCurrentNumber += 100;

                if(newCurrentNumber == 0 || (isGettingSmaller && newCurrentNumber >= currentNumber && currentNumber != 0) || (!isGettingSmaller && newCurrentNumber <= currentNumber)) zeroCounter++;
                currentNumber = newCurrentNumber;
            }
        }

        return zeroCounter;
    }

}
