package dev.phoenixofforce.adventofcode.y2023.day17;

import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import dev.phoenixofforce.adventofcode.solver.Dijkstra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class Day17_2023 implements Puzzle {

    private static boolean part2 = false;

    @Data @EqualsAndHashCode @AllArgsConstructor
    private static class State {
        private long x, y;

        private int movesInSameDirection = 0;
        private int direction = 0;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        State start = new State(0, 0, 0, -1);

        return Dijkstra
            .from(start)
            .to(s -> s.y == input.getLines().size() - 1 && s.x == input.getLines().get(0).length() - 1)
            .generateNextSteps(s -> getPaths(s, input.getLines().get(0).length(), input.getLines().size()))
            .withAccumulator((state, score) -> score + Integer.parseInt(input.getLines().get((int) state.y).charAt((int) state.x)+ ""))
            .getFirst().getDistance();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        part2 = true;
        return solvePart1(input);
    }

    private List<State> getPaths(State in, int maxX, int maxY) {
        if(in.direction == -1) {
            return List.of(
                new State(1, 0, 1, Direction.EAST.ordinal()),
                new State(0, 1, 1, Direction.SOUTH.ordinal())
            );
        }

        List<State> out = new ArrayList<>();
        Direction currentDirection = Direction.values()[in.getDirection()];

        if(in.movesInSameDirection < (part2? 10: 3)) {
            out.add(new State(
                in.x + currentDirection.getDx(),
                in.y + currentDirection.getDy(),
                in.movesInSameDirection + 1,
                in.direction
            ));
        }

        if(!part2 || in.movesInSameDirection >= 4) {
            for(Direction adjacentDirection: currentDirection.adjacent()) {
                out.add(new State(
                    in.x + adjacentDirection.getDx(),
                    in.y + adjacentDirection.getDy(),
                    1,
                    adjacentDirection.ordinal()
                ));
            }
        }

        return out.stream()
            .filter(s -> s.x >= 0 && s.x < maxX && s.y >= 0 && s.y < maxY)
            .toList();
    }
}
