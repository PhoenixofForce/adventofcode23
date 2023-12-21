package dev.phoenixofforce.adventofcode.y2023.day21;

import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day21_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        Position start = null;
        for(int y = 0; y < input.getLines().size(); y++) {
            for(int x = 0; x < input.getLines().get(0).length(); x++) {
                if(input.getChar(x, y) == 'S') {
                    start = new Position(x, y);
                    break;
                }
            }
        }

        Set<Position> currentPosition = new HashSet<>();
        currentPosition.add(start);

        for(int i = 0; i < 64; i++) {
            currentPosition = currentPosition.stream()
                .map(pos -> Direction.getNeighbors4((int) pos.getX(), (int) pos.getY()))
                .map(list -> list.stream().map(array -> new Position(array[0], array[1])).toList())
                .flatMap(Collection::stream)
                .distinct()
                .filter(e -> input.inbounds(e.getX(), e.getY()))
                .filter(e -> input.getChar(e.getX(), e.getY()) != '#')
                .collect(Collectors.toSet());
            System.out.println(i + ": " + currentPosition.size());
        }

        return currentPosition.size();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Position start = null;
        for(int y = 0; y < input.getLines().size(); y++) {
            for(int x = 0; x < input.getLines().get(0).length(); x++) {
                if(input.getChar(x, y) == 'S') {
                    start = new Position(x, y);
                    break;
                }
            }
        }

        Set<Position> currentPosition = new HashSet<>();
        currentPosition.add(start);

        List<Integer> formulaPoints = new ArrayList<>();
        for(int i = 1; i <= 65 + 2 * 131; i++) {
            currentPosition = currentPosition.stream()
                .map(pos -> Direction.getNeighbors4((int) pos.getX(), (int) pos.getY()))
                .map(list -> list.stream().map(array -> new Position(array[0], array[1])).toList())
                .flatMap(Collection::stream)
                .distinct()
                .filter(e -> input.getCharTranscending(e.getX(), e.getY()) != '#')
                .collect(Collectors.toSet());

            if((i-65) % 131 == 0) {
                formulaPoints.add(currentPosition.size());
            }
        }

        return BigDecimal.valueOf(expandQuadraticFormula(formulaPoints, (26501365 - 65) / 131));
    }

    private double expandQuadraticFormula(List<Integer> points, long wanted) {
        // p[i] = a *i^2 + b *i + c
        // i == 0 => c = p[0]
        double c = points.get(0);

        // p[1] = a * 1^2 + b * 1^1 + c
        // p[1] = a + b + c
        //   => a = p[1] - b - c

        // p[2] = a * 4^2 + b * 2^1 + c
        // p[2] = 4a + 2b + c
        // => p[2] = 4(p[1] - b - c) + 2b + c
        // => p[2] = 4p[1] - 2b - 3c
        // => b = (4p[1] - 3c - p[2]) / 2
        double b = (4 * points.get(1) - 3 * c - points.get(2)) / 2.0;
        double a = points.get(1) - b - c;
        return a * Math.pow(wanted, 2) + b * wanted + c;
    }
}