package dev.phoenixofforce.adventofcode.y2023.day11;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class Day11_2023 implements Puzzle {

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Position {
        private long x, y;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        return getExpansionDistance(input, 2);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return getExpansionDistance(input, 1000000);
    }

    private List<Integer> emptyColumns;
    private List<Integer> emptyRows;
    private long getExpansionDistance(PuzzleInput input, long expansionSize) {
        List<String> lines = input.getLines();

        //Expand
        if(emptyColumns == null || emptyRows == null) {
            emptyColumns = new ArrayList<>();
            emptyRows = new ArrayList<>();

            for(int i = 0; i < lines.size(); i++) {
                String current = lines.get(i);
                if(current.chars().noneMatch(e -> e == '#')) {
                    emptyRows.add(i);
                }
            }

            for(int i = 0; i < lines.get(0).length(); i++) {
                int finalI = i;
                if(lines.stream().noneMatch(line -> line.charAt(finalI) == '#')) {
                    emptyColumns.add(i);
                }
            }
        }

        //Distances
        List<Position> galaxies = new ArrayList<>();
        for(int y = 0; y < lines.size(); y++) {
            for(int x = 0; x < lines.get(y).length(); x++) {

                if(lines.get(y).charAt(x) == '#') {
                    int finalX = x;
                    long xDiff = emptyColumns.stream()
                        .filter(xl -> xl < finalX).count() * (expansionSize - 1);

                    int finalY = y;
                    long yDiff = emptyRows.stream()
                        .filter(yl -> yl < finalY).count() * (expansionSize - 1);

                    galaxies.add(new Position(x + xDiff, y + yDiff));
                }
            }
        }

        long out = 0;
        for(int i = 0; i < galaxies.size() - 1; i++) {
            Position galaxy1 = galaxies.get(i);
            for(int j = i + 1; j < galaxies.size(); j++) {
                Position galaxy2 = galaxies.get(j);

                out += Math.abs(galaxy2.x - galaxy1.x) + Math.abs(galaxy2.y - galaxy1.y);
            }
        }
        return out;
    }


}
