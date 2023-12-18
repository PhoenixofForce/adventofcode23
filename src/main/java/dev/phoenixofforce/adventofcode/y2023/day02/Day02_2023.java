package dev.phoenixofforce.adventofcode.y2023.day02;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

@Component
public class Day02_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        int targetRed = 12;
        int targetGreen = 13;
        int targetBlue = 14;

        return input.getLines().stream()
            .map(Game::new)
            .filter(g -> g.isPossibleWithGivenAmount(targetRed, targetGreen, targetBlue))
            .mapToInt(Game::getId)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return input.getLines().stream()
            .map(Game::new)
            .mapToLong(Game::getPower)
            .sum();
    }
}
