package dev.phoenixofforce.adventofcode.y2021.day04;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Day04_2021 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return run(input.getParagraphs(), true);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return run(input.getParagraphs(), false);
    }

    private int run(List<String> lines, boolean isDay1) {
        List<Integer> randomNumbers = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).toList();
        List<BingoBoard> boards = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) boards.add(new BingoBoard(lines.get(i)));

        int winner = 0;
        for (int drawn : randomNumbers) {
            for (BingoBoard b : boards) {
                if (b.drawNumber(drawn)) {
                    if (isDay1 || winner == boards.size() - 1) {
                        return b.getUnmarkedSum() * drawn;
                    }
                    winner++;
                }
            }

            if (winner == boards.size()) break;
        }

        return -1;
    }

}
