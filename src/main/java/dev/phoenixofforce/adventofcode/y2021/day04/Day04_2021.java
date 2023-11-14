package dev.phoenixofforce.adventofcode.y2021.day04;

import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Day04_2021 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<String> lines = input.getParagraphs();

        List<Integer> randomNumbers = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).toList();
        List<BingoBoard> boards = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) boards.add(new BingoBoard(lines.get(i)));

        int winner = 0;
        for (int drawn : randomNumbers) {
            for (BingoBoard b : boards) {
                if (b.drawNumber(drawn)) {
                    return b.getUnmarkedSum() * drawn;
                }
            }

            if (winner == boards.size()) break;
        }
        return "error";
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<String> lines = input.getParagraphs();

        List<Integer> randomNumbers = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).toList();
        List<BingoBoard> boards = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) boards.add(new BingoBoard(lines.get(i)));

        int winner = 0;
        for (int drawn : randomNumbers) {
            for (BingoBoard b : boards) {
                if (b.drawNumber(drawn)) {
                    if (winner == boards.size() - 1) {
                        return b.getUnmarkedSum() * drawn;
                    }
                    winner++;
                }
            }

            if (winner == boards.size()) break;
        }
        return "error";
    }

}
